function getAdmins() {
    
    axios.get('http://localhost:8080/admins')
    .then(response => {
        document.getElementById('admins-list').innerHTML = JSON.stringify(response.data);
    })
    .catch(error => {
        console.log(error);
    })
}

function createAdmin() {
    var username = document.getElementById("admins-username").value;
    var password = document.getElementById("admins-password").value;
    var email = document.getElementById("admins-email").value;

    if (username !== "" && password !== "" && email !== "") {
        const data = {
            username: username,
            password: password,
            email: email,
        };

        axios.post('http://localhost:8080/admins', data)
            .then(response => {
                document.getElementById('admins-response').style.color = "black";
                document.getElementById('admins-response').innerHTML = 'Ответ: ' + JSON.stringify(response.data);
                getAdmins();
                document.getElementById("admins-username").value = "";
                document.getElementById("admins-password").value = "";
                document.getElementById("admins-email").value = "";
            })
            .catch(error => {
                document.getElementById('admins-response').style.color = "red";
                document.getElementById('admins-response').innerHTML = 'Ошибка: ' + JSON.stringify(error.response.data.message);
            })
    }

}

function deleteAdmins() {
    var id = document.getElementById('admins-id').value;
    axios.delete(`http://localhost:8080/admins/${id}`)
            .then(response => {
                document.getElementById('admins-response').style.color = "black";
                getAdmins();
            })
            .catch(error => {
                document.getElementById('admins-response').style.color = "red";
                document.getElementById('admins-delete-response').innerHTML = 'Ошибка: ' + JSON.stringify(error.response.data.message);
            })

    document.getElementById('admins-id').value = "";
}

function trainingsСonducted() {
    axios.get('http://localhost:8080/admins/trainings-conducted')
    .then(response => {
        document.getElementById('admins-trainings-conducted-response').innerHTML = JSON.stringify(response.data);
    })
    .catch(error => {
        console.log(error);
    })
}

function clientsActivity() {
    axios.get('http://localhost:8080/admins/clients-activity')
    .then(response => {
        document.getElementById('admins-clients-activity-response').innerHTML = JSON.stringify(response.data);
    })
    .catch(error => {
        console.log(error);
    })
}

function trainersActivity() {
    axios.get('http://localhost:8080/admins/trainers-activity')
    .then(response => {
        document.getElementById('admins-trainers-activity-response').innerHTML = JSON.stringify(response.data);
    })
    .catch(error => {
        console.log(error);
    })
}