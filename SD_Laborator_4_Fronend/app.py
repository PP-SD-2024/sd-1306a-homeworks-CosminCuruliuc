from flask import Flask, render_template, request, redirect, url_for
import requests

app = Flask(__name__)

BACKEND_URL = "http://localhost:8080"


@app.route('/')
def index():
    return render_template('index.html')


@app.route('/login', methods=['GET', 'POST'])
def login():
    if request.method == 'POST':
        username = request.form['username']
        password = request.form['password']
        response = requests.post(f"{BACKEND_URL}/users/login", json={"username": username, "password": password})
        if response.status_code == 200:
            return redirect(url_for('expenses'))
        else:
            return "Login Failed", 401
    return render_template('login.html')


@app.route('/register', methods=['GET', 'POST'])
def register():
    if request.method == 'POST':
        username = request.form['username']
        password = request.form['password']
        email = request.form['email']
        response = requests.post(f"{BACKEND_URL}/users/register",
                                 json={"username": username, "password": password, "email": email})
        if response.status_code == 201:
            return redirect(url_for('login'))
        else:
            return "Registration Failed", 400
    return render_template('register.html')


@app.route('/expenses/add', methods=['GET', 'POST'])
def add_expense():
    if request.method == 'POST':
        userId = request.form['userId']
        category = request.form['category']
        amount = request.form['amount']
        date = request.form['date']
        description = request.form['description']
        response = requests.post(f"{BACKEND_URL}/expenses", json={
            "userId": userId,
            "category": category,
            "amount": amount,
            "date": date,
            "description": description
        })
        if response.status_code == 201:
            return redirect(url_for('expenses'))
        else:
            return "Failed to add expense", 400
    return render_template('add_expense.html')


@app.route('/expenses', methods=['GET'])
def expenses():
    userId = 1
    expenses = requests.get(f"{BACKEND_URL}/expenses/user/{userId}")
    if expenses.status_code == 200:
        return render_template('expenses.html', expenses=expenses.json())
    return "Failed to retrieve expenses", 400


if __name__ == '__main__':
    app.run(debug=True)
