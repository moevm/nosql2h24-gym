function getTrainings() {
    
    axios.get('http://localhost:8080/trainers/trainings')
    .then(response => {
        document.getElementById('trainigs-list').innerHTML = JSON.stringify(response.data);

        const selectElement = document.getElementById('clients-trainigs');

        selectElement.innerHTML = '';

        const defaultOption = document.createElement('option');
        defaultOption.textContent = 'Выберите тренировку';
        defaultOption.value = '';
        selectElement.appendChild(defaultOption);

        response.data.forEach(trainig => {
            const option = document.createElement('option');
            option.textContent = trainig.startTime + "_" + trainig.endTime;
            option.value = trainig.id + "_" + trainig.trainerId;
            selectElement.appendChild(option);
        });
    })
    .catch(error => {
        console.log(error);
    })

}

function createTraining() {
    var startDate = document.getElementById("trainings-start").value;
    var endDate = document.getElementById("trainings-end").value;
    var trainerId = document.getElementById("trainings-trainer-id").value;

    if (startDate !== "" && endDate !== "" && trainerId !== "") {
        const data = {
            startTime: startDate,
            endTime: endDate
        };

        axios.post(`http://localhost:8080/trainers/${trainerId}/trainings`, data)
        .then(response => {
            document.getElementById('trainigs-response').style.color = "black";
            document.getElementById('trainigs-response').innerHTML = 'Ответ: ' + JSON.stringify(response.data);
            getTrainings();
            document.getElementById("trainings-start").value = "";
            document.getElementById("trainings-end").value = "";
            document.getElementById("trainings-trainer-id").value = "";
        })
        .catch(error => {
            console.log(error)
            document.getElementById('trainigs-response').style.color = "red";
            document.getElementById('trainigs-response').innerHTML = 'Ошибка: ' + JSON.stringify(error.response.data.message);
        })
    }
}

function deleteTrainigs() {
    var trainigId = document.getElementById("trainings-id").value;

    axios.delete(`http://localhost:8080/trainers/trainings/${trainigId}`)
        .then(response => {
            document.getElementById('trainigs-response').style.color = "black";
            getTrainings();
        })
        .catch(error => {
            document.getElementById('trainigs-response').style.color = "red";
            document.getElementById('trainigs-delete-response').innerHTML = 'Ошибка: ' + JSON.stringify(error.response.data.message);
        })

    document.getElementById('trainings-id').value = "";
}