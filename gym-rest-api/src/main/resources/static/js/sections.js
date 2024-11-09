function createSection() {
    var sectionName = document.getElementById("sections-name").value;
    if (sectionName != null) {
        const data = {name : sectionName};

        axios.post('http://localhost:8080/sections', data)
                .then(response => {
                    document.getElementById('sections-response').style.color = "black";
                    document.getElementById('sections-response').innerHTML = 'Ответ: ' + JSON.stringify(response.data);
                    getSections();
                    document.getElementById("sections-name").value = "";
                })
                .catch(error => {
                    document.getElementById('sections-response').style.color = "red";
                    document.getElementById('sections-response').innerHTML = 'Ошибка: ' + JSON.stringify(error.response.data.message);
                })
    }

}

function getSections() {
    axios.get('http://localhost:8080/sections')
            .then(response => {
                document.getElementById('sections-list').innerHTML = JSON.stringify(response.data);
                const selectElement = document.getElementById('trainers-sections');

                selectElement.innerHTML = '';

                response.data.forEach(section => {
                    const option = document.createElement('option');
                    option.textContent = section.name;
                    option.value = section.id;
                    selectElement.appendChild(option);
                });
            })
            .catch(error => {
                console.log(error);
            })
}

function deleteSection() {
    var id = document.getElementById('sections-id').value;
    axios.delete(`http://localhost:8080/sections/${id}`)
            .then(response => {
                document.getElementById('sections-response').style.color = "black";
                getSections();
            })
            .catch(error => {
                document.getElementById('sections-response').style.color = "red";
                document.getElementById('sections-delete-response').innerHTML = 'Ошибка: ' + JSON.stringify(error.response.data.message);
            })

    document.getElementById('sections-id').value = "";
}