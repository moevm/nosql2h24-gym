function getTrainers() {
    axios.get('http://localhost:8080/trainers')
            .then(response => {
                document.getElementById('trainers-list').innerHTML = JSON.stringify(response.data);
            })
            .catch(error => {
                console.log(error);
            })
}

function createTrainer() {
    var username = document.getElementById("trainers-username").value;
    var password = document.getElementById("trainers-password").value;
    var email = document.getElementById("trainers-email").value;
    var phone = document.getElementById("trainers-phone").value;

    let sectionsList = new Array();

    for (let i = 0; i < sections.length; i++) {
        sectionsList.push(sections[i].innerHTML);
    }

    if (username !== "" && password !== "" && email !== "" && experience !== "") {
        const data = {
                username: username,
                password: password,
                email: email,
                phoneNumber: phone
        };

        axios.post('http://localhost:8080/trainers', data)
                .then(response => {
                    document.getElementById('trainers-response').style.color = "black";
                    document.getElementById('trainers-response').innerHTML = 'Ответ: ' + JSON.stringify(response.data);
                    getTrainers();
                    document.getElementById("trainers-username").value = "";
                    document.getElementById("trainers-password").value = "";
                    document.getElementById("trainers-email").value = "";
                    document.getElementById("trainers-phone").value = "";
                })
                .catch(error => {
                    document.getElementById('trainers-response').style.color = "red";
                    document.getElementById('trainers-response').innerHTML = 'Ошибка: ' + JSON.stringify(error.response.data.message);
                })
    }
}

function deleteTrainer() {
    var id = document.getElementById('trainers-id').value;
    axios.delete(`http://localhost:8080/trainers/${id}`)
            .then(response => {
                document.getElementById('trainers-response').style.color = "black";
                getTrainers();
            })
            .catch(error => {
                document.getElementById('trainers-response').style.color = "red";
                document.getElementById('trainers-delete-response').innerHTML = 'Ошибка: ' + JSON.stringify(error.response.data.message);
            })

    document.getElementById('trainers-id').value = "";
}

function setHourlyRate() {
    var id = document.getElementById("trainers-id2").value;
    var hourly = document.getElementById("hourly-rate").value;

    if (id != "" && hourly != "&&") {
        data = {
            hourlyRate: hourly
        };

        axios.put(`http://localhost:8080/trainers/${id}/set-hourly-rate`, data)
                .then(response => {
                    document.getElementById('trainers-response-hourly').style.color = "black";
                    document.getElementById('trainers-response-hourly').innerHTML = 'Ответ: ' + JSON.stringify(response.data);
                    getTrainers();
                })
                .catch(error => {
                    document.getElementById('trainers-response-hourly').style.color = "red";
                    document.getElementById('trainers-response-hourly').innerHTML = 'Ошибка: ' + JSON.stringify(error.response.data.message);
                })
    }
}