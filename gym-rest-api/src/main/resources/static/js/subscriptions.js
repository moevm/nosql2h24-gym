function getSubscriptions() {
    
    axios.get('http://localhost:8080/subscriptions')
    .then(response => {
        document.getElementById('subscriptions-list').innerHTML = JSON.stringify(response.data);
    })
    .catch(error => {
        console.log(error);
    })

}

function createSubscription() {
    var clientId = document.getElementById("subscriptions-client-id").value;
    var duration = document.getElementById("subscriptions-duration").value;
    
    if (clientId !== "" && duration !== "") {
        const data = {
            duration: duration
        };

        axios.post(`http://localhost:8080/subscriptions/client/${clientId}`, data)
        .then(response => {
            document.getElementById('subscriptions-response').style.color = "black";
            document.getElementById('subscriptions-response').innerHTML = 'Ответ: ' + JSON.stringify(response.data);
            getSubscriptions();
            document.getElementById("subscriptions-client-id").value = "";
            document.getElementById("subscriptions-duration").value = "";
        })
        .catch(error => {
            document.getElementById('subscriptions-response').style.color = "red";
            document.getElementById('subscriptions-response').innerHTML = 'Ошибка: ' + JSON.stringify(error.response.data.message);
        })
    }
}

function deleteSubscription() {
    var id = document.getElementById('subscriptions-id').value;
    axios.delete(`http://localhost:8080/subscriptions/${id}`)
            .then(response => {
                document.getElementById('subscriptions-response').style.color = "black";
                getSubscriptions();
            })
            .catch(error => {
                document.getElementById('subscriptions-response').style.color = "red";
                document.getElementById('subscriptions-delete-response').innerHTML = 'Ошибка: ' + JSON.stringify(error.response.data.message);
            })

    document.getElementById('subscriptions-id').value = "";
}