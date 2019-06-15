from flask import Flask, render_template, request, abort, make_response, redirect, url_for, jsonify
import requests
import json
import jwt
from base64 import b64decode

app = Flask(__name__, static_url_path='')

BASE_URL = "http://gateway:8080"


@app.route('/', methods=['GET'])
def index():
    return make_response(redirect(url_for('all_jobs')))


@app.route('/dashboard', methods=['GET'])
def dashboard():

    j = validate(request)

    if not j:
        return render_template('index.html')

    user = {'username': j['sub'], 'role': j['authorities'][0]}

    header = {"authorization": "Bearer " + j['token']}

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
                BASE_URL + "/api/centers/" +
                user['username'] + "/jobs/" + str(a),
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


@app.route('/browse', methods=['GET'])
def all_jobs():
    j = validate(request)

    if not j:
        user = {}
        header = {}

    else:
        user = {'username': j['sub'], 'role': j['authorities'][0]}
        header = {"authorization": "Bearer " + j['token']}

    if 'q' not in request.args:
        params = dict()
    else:
        params = request.args.to_dict(flat=True)

    jobs = requests.get(
        BASE_URL + "/api/jobs/search",
        headers=header,
        params=params
    ).json()

    return render_template('all_jobs.html', user=user, jobs=jobs, name_page='Job Portal')


@app.route('/jobcenters', methods=['GET', 'POST'])
def jobcenter_list():
    j = validate(request)

    if not j:
        abort(401)

    user = {'username': j['sub'], 'role': j['authorities'][0]}

    header = {"authorization": "Bearer " + j['token']}

    jobcenters = requests.get(
        BASE_URL + "/api/centers/",
        headers=header
    ).json()

    print(jobcenters)

    return render_template('jobcenter_list.html', user=user, jobcenters=jobcenters)


@app.route('/seekers/<s_username>', methods=['GET', 'POST'])
def seeker_detail(s_username):
    j = validate(request)

    if not j:
        abort(401)

    user = {'username': j['sub'], 'role': j['authorities'][0]}

    header = {"authorization": "Bearer " + j['token']}

    # Esistenza jobcenter
    seeker = requests.get(
        BASE_URL + "/api/seekers/" + s_username,
        headers=header,
    )

    if seeker.status_code != 200:
        abort(seeker.status_code)

    if request.method == 'POST' and s_username == user['username']:
        r_json = request.form.to_dict(flat=True)
        r_json["username"] = user['username']
        r_json["type"] = "SEEKER"
        r_json["skills"] = request.form.getlist("skills")
        #del r_json["skills"]

        if r_json["password"] == '':
            del r_json["password"]

        print(r_json)
        r = requests.put(
            BASE_URL + "/users/" + s_username,
            headers=header,
            json=r_json
        )

        print(r)

    seeker = requests.get(
        BASE_URL + "/api/seekers/" + s_username,
        headers=header,
    )

    seeker = seeker.json()
    print(seeker)
    if request.method == 'POST' and 'delete' in request.form:
        r = requests.delete(
            BASE_URL + "/users/" + s_username,
            headers=header
        )
        resp = make_response(redirect(url_for('index')))
        resp.set_cookie('bearer', '', expires=0)
        return resp
    return render_template('seeker_detail.html', user=user, seeker=seeker)


@app.route('/jobcenter/<j_username>', methods=['GET', 'POST'])
def jobcenter_detail(j_username):
    # Validazione
    j = validate(request)

    if not j:
        user = {}
        header = {}
    else:
        user = {'username': j['sub'], 'role': j['authorities'][0]}
        header = {"authorization": "Bearer " + j['token']}

    # Esistenza jobcenter
    jobcenter = requests.get(
        BASE_URL + "/api/centers/" + j_username,
        headers=header,
    )

    if jobcenter.status_code != 200:
        abort(jobcenter.status_code)

    # TODO: Controllo
    if request.method == 'POST' and j_username == user['username']:
        r_json = request.form.to_dict(flat=True)
        r_json["type"] = "JOB_CENTER"
        r_json["username"] = user['username']

        if r_json["password"] == '':
            del r_json["password"]

        r = requests.put(
            BASE_URL + "/users/" + j_username,
            headers=header,
            json=r_json
        )
        if r.status_code != 200:
            abort(r.status_code)
        print(r)

    jobcenter = requests.get(
        BASE_URL + "/api/centers/" + j_username,
        headers=header,
    )
    jobcenter = jobcenter.json()

    jobs = requests.get(
        BASE_URL + "/api/centers/" + j_username + "/jobs/",
        headers=header
    ).json()

    jobcenter['jobs'] = jobs

    print(jobcenter)

    if request.method == 'POST' and 'delete' in request.form:
        r = requests.delete(
            BASE_URL + "/users/" + j_username,
            headers=header
        )
        resp = make_response(redirect(url_for('index')))
        resp.set_cookie('bearer', '', expires=0)
        return resp

    return render_template('jobcenter_detail.html', user=user, jobcenter=jobcenter)


@app.route('/jobs/new', methods=['GET', 'POST'])
def newJobs():
    j = validate(request)

    if not j:
        abort(401)

    user = {'username': j['sub'], 'role': j['authorities'][0]}
    header = {"authorization": "Bearer " + j['token']}

    if request.method == 'POST':
        r_json = request.form.to_dict(flat=True)
        r_json["username"] = user["username"]
        r_json["skills"] = request.form.getlist("skills")
        r = requests.post(
            BASE_URL + "/api/centers/" + user["username"] + "/jobs/",
            headers=header,
            json=r_json
        )
        print(r)

        if r.status_code != 201:
            abort(r.status_code)
        else:
            return make_response(redirect(url_for('dashboard')))

    return render_template("new_job.html", user=user, job={})


@app.route("/logout", methods=['GET'])
def logout():
    resp = make_response(render_template('logout.html'))
    resp.set_cookie('bearer', '', expires=0)
    return resp


@app.route('/jobcenters/<j_username>/job/<job_id>', methods=['GET', 'POST'])
def job_detail(j_username, job_id):
    # Validazione
    j = validate(request)

    if not j:
        user = {}
        header = {}

    else:
        user = {'username': j['sub'], 'role': j['authorities'][0]}
        header = {"authorization": "Bearer " + j['token']}

    # Sono seeker e mi sto applicando
    if request.method == 'POST' and 'apply' in request.form:

        if user == {}:
            return render_template('login.html')

        r = requests.post(
            BASE_URL + "/api/seekers/" + user["username"] + "/applications/",
            headers=header,
            json={"username": user["username"], "jobId": job_id}
        )

        if r.status_code != 201:
            abort(r.status_code)
        else:
            return make_response(redirect(url_for('dashboard')))

    elif request.method == 'POST' and 'position' in request.form:
        data = request.form.to_dict(flat=True)
        data["id"] = job_id
        data["username"] = user["username"]
        data["skills"] = request.form.getlist("skills")

        r = requests.put(
            BASE_URL + "/api/centers/" + user["username"] + "/jobs/" + job_id,
            headers=header,
            json=data
        )

        print("****************** RESPONSE: ", r)

        print("****************** DATA: ", data)

    job = requests.get(
        BASE_URL + "/api/centers/" + j_username + "/jobs/" + job_id,
        headers=header
    )

    job = job.json()

    if user and user["role"] == "JOB_CENTER" and user["username"] == j_username:
        applications = requests.get(
            BASE_URL + "/api/centers/" + j_username + "/jobs/" + job_id + "/applications",
            headers=header
        )

        applications = applications.json()

        job["applications"] = []

        for a in applications:
            s_username = a["username"]

            seeker = requests.get(
                BASE_URL + "/api/seekers/" + s_username,
                headers=header
            ).json()
            job["applications"] += [seeker]

    if request.method == 'POST' and 'delete' in request.form:
        r = requests.delete(
            BASE_URL + "/api/centers/" + j_username + "/jobs/" + job_id,
            headers=header
        )
        resp = make_response(redirect(url_for('dashboard')))
        return resp
    return render_template('job_detail.html', user=user, job=job)


@app.route('/login', methods=['GET', 'POST'])
def login():
    if request.method == 'POST':
        data = request.form.to_dict(flat=True)

        if not data:
            data = request.args["cred"]

        r = requests.post(BASE_URL + "/token/generate-token", json=data)

        if r.status_code == 200:
            r_json = r.json()
            token = r_json['result']['token']
            print(token)

            resp = make_response(redirect(url_for('dashboard')))
            resp.set_cookie('bearer', token)

            return resp
        else:
            abort(r.status_code)

    return render_template('login.html')


@app.route('/signup/jobcenter', methods=['GET', 'POST'])
def signup_jobcenter():
    if request.method == 'POST':
        r_json = request.form.to_dict(flat=True)
        r_json["type"] = "JOB_CENTER"
        print(r_json)
        print(requests.post(BASE_URL + "/signup", json=r_json))

        return redirect(url_for('login', cred={'username': r_json['username'], 'password': r_json['password']}), code=307)

    return render_template('signup_jobcenter.html')


@app.route('/signup/seeker', methods=['GET', 'POST'])
def signup_seeker():

    if request.method == 'POST':
        r_json = request.form.to_dict(flat=True)
        r_json["type"] = "SEEKER"
        # return jsonify(request.form.getlist("skills"))
        r_json["skills"] = request.form.getlist("skills")
        #del r_json["skill"]

        r = requests.post(
            BASE_URL + "/signup",
            json=r_json
        )

        print(r)

        cred = {'username': r_json['username'], 'password': r_json['password']}

        # Piglio il Token
        r = requests.post(
            BASE_URL + "/token/generate-token",
            json=cred
        ).json()

        token = r['result']['token']

        file = request.files['cv']

        # Mando il cv
        r = requests.post(
            BASE_URL + "/api/seekers/" + r['result']["username"] + "/cv",
            headers={"authorization": "Bearer " + token},
            files={"file": file}
        )

        print(r)

        return redirect(url_for('login', cred=cred), code=307)

    return render_template('signup_seeker.html')


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
