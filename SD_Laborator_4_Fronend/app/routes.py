from flask import Blueprint, render_template, request, redirect, url_for, session, flash, jsonify
import requests
import json

main = Blueprint('main', __name__)

BACKEND_URL = "http://localhost:8080"


@main.route('/')
def index():
    if 'username' in session:
        headers = {'Authorization': f"Bearer {session.get('auth_token')}"}
        response = requests.get(f"{BACKEND_URL}/expenses/user/{session['username']}", headers=headers)
        try:
            expenses = response.json() if response.status_code == 200 else []
        except json.JSONDecodeError:
            expenses = []
    else:
        expenses = []
    return render_template('index.html', expenses=expenses)


@main.route('/login', methods=['GET', 'POST'])
def login():
    if request.method == 'POST':
        user_data = {
            'username': request.form['username'],
            'password': request.form['password']
        }
        response = requests.post(f"{BACKEND_URL}/auth/login", json=user_data)
        if response.status_code == 200:
            session['auth_token'] = response.text
            session['username'] = request.form['username']
            return redirect(url_for('main.index'))
        else:
            flash('Nume de utilizator sau parolă incorecte.')
    return render_template('login.html')


@main.route('/register', methods=['GET', 'POST'])
def register():
    if request.method == 'POST':
        user_data = {
            'username': request.form['username'],
            'password': request.form['password'],
            'firstName': request.form['FirstName'],
            'lastName': request.form['LastName']
        }
        response = requests.post(f"{BACKEND_URL}/auth/register", json=user_data)
        if response.status_code == 200:
            return redirect(url_for('main.login'))
        else:
            flash('Eroare la înregistrare. Încercați din nou.')
    return render_template('register.html')


@main.route('/logout')
def logout():
    session.clear()
    return redirect(url_for('main.index'))


@main.route('/expenses/add', methods=['GET', 'POST'])
def add_expense():
    if 'auth_token' not in session:
        return redirect(url_for('main.login'))

    if request.method == 'POST':
        expense_data = {
            'username': session['username'],
            'category': request.form['category'],
            'amount': request.form['amount'],
            'date': request.form['date']
        }
        headers = {'Authorization': f"Bearer {session['auth_token']}"}
        response = requests.post(f"{BACKEND_URL}/expenses/add", json=expense_data, headers=headers)
        if response.status_code == 200:
            flash('Cheltuială adăugată cu succes.')
            return redirect(url_for('main.index'))
        else:
            flash('Eroare la adăugarea cheltuielii. Încercați din nou.')
    return render_template('add_expense.html')
