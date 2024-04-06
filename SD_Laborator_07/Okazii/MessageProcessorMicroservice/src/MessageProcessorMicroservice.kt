import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.ServerSocket
import java.net.Socket
import java.util.*
import kotlin.system.exitProcess

class MessageProcessorMicroservice {
    private var messageProcessorSocket: ServerSocket
    private lateinit var biddingProcessorSocket: Socket
    private var auctioneerConnection:Socket
    private var receiveInQueueObservable: Observable<String>
    private val subscriptions = CompositeDisposable()
    private val messageQueue: Queue<Message> = LinkedList<Message>()

    companion object Constants {
        const val MESSAGE_PROCESSOR_PORT = 1600
        const val BIDDING_PROCESSOR_HOST = "localhost"
        const val BIDDING_PROCESSOR_PORT = 1700
    }

    init {
        messageProcessorSocket = ServerSocket(MESSAGE_PROCESSOR_PORT)
        println("MessageProcessorMicroservice se executa pe portul: ${messageProcessorSocket.localPort}")
        println("Se asteapta mesaje pentru procesare...")

        // se asteapta mesaje primite de la AuctioneerMicroservice
        auctioneerConnection = messageProcessorSocket.accept()
        val bufferReader = BufferedReader(InputStreamReader(auctioneerConnection.inputStream))

        // se creeaza obiectul Observable cu care se captureaza mesajele de la AuctioneerMicroservice
        receiveInQueueObservable = Observable.create<String> { emitter ->
            while (true) {
                // se citeste mesajul de la AuctioneerMicroservice de pe socketul TCP
                val receivedMessage = bufferReader.readLine()

                // daca se primeste un mesaj gol (NULL), atunci inseamna ca cealalta parte a socket-ului a fost inchisa
                if (receivedMessage == null) {
                    // deci subscriber-ul respectiv a fost deconectat
                    bufferReader.close()
                    auctioneerConnection.close()

                    emitter.onError(Exception("Eroare: AuctioneerMicroservice ${auctioneerConnection.port} a fost deconectat."))
                    break
                }

                // daca mesajul este cel de incheiere a licitatiei (avand corpul "final"), atunci se emite semnalul Complete
                if (Message.deserialize(receivedMessage.toByteArray()).body == "final") {
                    emitter.onComplete()

                    break
                } else {
                    // se emite ce s-a citit ca si element in fluxul de mesaje
                    emitter.onNext(receivedMessage)
                }
            }
        }
    }

    private fun receiveAndProcessMessages() {
        val receiveInQueueSubscription = receiveInQueueObservable
            .map { Message.deserialize(it.toByteArray()) } // Convertim fiecare String primit intr-un obiect Message.
            .distinctUntilChanged { a, b -> a.body == b.body } // Filtram duplicatele bazat pe continutul mesajului.
            .toList() // Convertim fluxul de date intr-o lista pentru a putea aplica sortarea.
            .subscribeBy(
                onSuccess = { messages ->
                    // Sortam mesajele in functie de timestamp.
                    val sortedMessages = messages.sortedBy { it.timestamp }
                    messageQueue.addAll(sortedMessages)

                    println("Mesaje primite și procesate:")
                    sortedMessages.forEach { println(it) }

                    // Continuam cu trimiterea mesajelor procesate catre BiddingProcessor.
                    sendProcessedMessages()
                },
                onError = { println("Eroare: $it") }
            )
        subscriptions.add(receiveInQueueSubscription)
    }


    private fun sendProcessedMessages() {
        try {
            biddingProcessorSocket = Socket(BIDDING_PROCESSOR_HOST, BIDDING_PROCESSOR_PORT)

            println("Trimit urmatoarele mesaje:")
            Observable.fromIterable(messageQueue).subscribeBy(
                onNext = {
                    println(it.toString())

                    // trimitere mesaje catre procesorul licitatiei, care decide rezultatul final
                    biddingProcessorSocket.getOutputStream().write(it.serialize())
                },
                onComplete = {
                    val noMoreMessages = Message.create(
                        "${biddingProcessorSocket.localAddress}:${biddingProcessorSocket.localPort}",
                        "final"
                    )
                    biddingProcessorSocket.getOutputStream().write(noMoreMessages.serialize())
                    biddingProcessorSocket.close()

                    // se elibereaza memoria din multimea de Subscriptions
                    subscriptions.dispose()
                }
            )
        } catch (e: Exception) {
            println("Nu ma pot conecta la BiddingProcessor!")
            messageProcessorSocket.close()
            exitProcess(1)
        }
    }

    fun run() {
        receiveAndProcessMessages()
    }
}

fun main(args: Array<String>) {
    val messageProcessorMicroservice = MessageProcessorMicroservice()
    messageProcessorMicroservice.run()
}