window.onload = function() {
    Promise.all([getSections(), getTrainers(), getTrainings(), getClients(), getAdmins(), getSubscriptions()])
        .then(() => {
            console.log('Обе функции успешно выполнены!');
        })
        .catch(error => {
            console.error('Произошла ошибка при выполнении функций:', error);
        });
};