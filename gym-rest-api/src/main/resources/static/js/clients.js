function getClients() {
    
    axios.get('http://localhost:8080/clients')
    .then(response => {
        document.getElementById('clients-list').innerHTML = JSON.stringify(response.data);
    })
    .catch(error => {
        console.log(error);
    })

}

function createClient() {
    var username = document.getElementById("clients-username").value;
    var password = document.getElementById("clients-password").value;
    var email = document.getElementById("clients-email").value;
    var phone = document.getElementById("clients-phone").value;

    if (username !== "" && password !== "" && email !== "" && phone !== "") {
        const data = {
            username: username,
            password: password,
            email: email,
            phone: phone
        };

        axios.post('http://localhost:8080/clients', data)
            .then(response => {
                document.getElementById('clients-response').style.color = "black";
                document.getElementById('clients-response').innerHTML = 'Ответ: ' + JSON.stringify(response.data);
                getClients();
                document.getElementById("clients-username").value = "";
                document.getElementById("clients-password").value = "";
                document.getElementById("clients-email").value = "";
                document.getElementById("clients-phone").value = "";
            })
            .catch(error => {
                document.getElementById('clients-response').style.color = "red";
                document.getElementById('clients-response').innerHTML = 'Ошибка: ' + JSON.stringify(error.response.data.message);
            })
    }

}

function deleteClients() {
    var id = document.getElementById('clients-id').value;
    axios.delete(`http://localhost:8080/clients/${id}`)
            .then(response => {
                document.getElementById('clients-response').style.color = "black";
                getClients();
            })
            .catch(error => {
                document.getElementById('clients-response').style.color = "red";
                document.getElementById('clients-delete-response').innerHTML = 'Ошибка: ' + JSON.stringify(error.response.data.message);
            })

    document.getElementById('clients-id').value = "";
}

function registrationClients() {
    var clientId = document.getElementById("clients-registration-id").value;
    var trainigs = document.getElementById("clients-trainigs").value;

    if (clientId !== "" && trainigs !== "") {
        var trainerId = trainigs.split("_")[1];
        var trainingId = trainigs.split("_")[0];
        axios.post(`http://localhost:8080/trainers/${trainerId}/trainings/${trainingId}/registration/${clientId}`)
            .then(response => {
                document.getElementById('clients-registration-response').style.color = "black";
                document.getElementById('clients-registration-response').innerHTML = 'Ответ: ' + JSON.stringify(response.data);
                getTrainers();
                document.getElementById("clients-trainigs").value = "";
                document.getElementById("clients-registration-id").value = "";
            })
            .catch(error => {
                document.getElementById('clients-registration-response').style.color = "red";
                document.getElementById('clients-registration-response').innerHTML = 'Ошибка: ' + JSON.stringify(error.response.data.message);
            })
    }
}