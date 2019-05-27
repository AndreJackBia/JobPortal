from flask import Flask, render_template, request, abort, make_response, redirect, url_for
import requests
import json
import jwt
from base64 import b64decode

app = Flask(__name__, static_url_path='')

BASE_URL = "http://gateway:8080"


@app.route('/', methods=['GET'])
def index():
    return render_template('index.html')

@app.route('/dashboard', methods = ['GET'])
def dashboard():

    j = validate(request)

    if not j:
        abort(401)

    user = {'username' : j['sub'], 'role' : j['authorities'][0]}

    header = { "authorization" : "Bearer " + j['token']}

    if user['role'] == 'SEEKER':
        r_appl = requests.get(
                BASE_URL + "/api/seekers/" + user['username'] + "/applications/",
                headers=header
            )
        print(r_appl.json())
        appl_id = [a['jobId'] for a in r_appl.json()]

        applications = []

        for a in appl_id:
            job = requests.get(
                BASE_URL + "/api/centers/" + user['username'] + "/jobs/" + str(a),
                headers=header
            )
            applications += [job.json()]

        user["applications"] = applications

        r_sugg = requests.get(
            BASE_URL + "/api/seekers/" + user['username'] + "/suggestions",
            headers=header
        )

        user['suggestions'] = r_sugg.json()

    elif user['role'] == 'JOB_CENTER':
        jobs = requests.get(
                BASE_URL + "/api/centers/" + user['username'] + "/jobs/",
                headers=header
            ).json()

        user['jobs'] = jobs



    print(user)

    return render_template('dashboard.html', user=user, name_page='Dashboard')

@app.route('/alljobs', methods = ['GET'])
def all_jobs():
	return 0

@app.route('/jobcenters', methods = ['GET', 'POST'])
def jobcenter_list():
	return 0

@app.route('/seekers/<s_username>', methods = ['GET', 'POST'])
def seeker_detail(s_username):
	return 0

@app.route('/jobcenter/<j_username>', methods = ['GET', 'POST'])
def jobcenter_detail(j_username):
	return 0

@app.route("/logout", methods=['GET'])
def logout():
	return 0

@app.route('/jobs/new', methods = ['GET', 'POST'])
def newJobs():
	return 0

@app.route('/jobcenters/<j_username>/job/<job_id>', methods = ['GET', 'POST'])
def job_detail(j_username, job_id):
	return 0

def validate(request):

    try:
        token = request.cookies.get('bearer')
        j = jwt.decode(token, b64decode("JwtSecretKey"), algorithms=['HS256'])
        j['token'] = token
    except:
        j = {}

    return j


if __name__ == '__main__':
    app.run(debug=True)
