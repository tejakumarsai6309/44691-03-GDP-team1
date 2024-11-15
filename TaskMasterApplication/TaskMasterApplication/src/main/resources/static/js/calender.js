/**
 * 
 */
document.addEventListener('DOMContentLoaded', function() {
    const calendarBody = document.getElementById('calendar-body');
    const tasks = JSON.parse('[[${tasks}]]');

    function createCalendar(year, month) {
        let date = new Date(year, month);
        let firstDay = new Date(year, month, 1).getDay();
        let lastDate = new Date(year, month + 1, 0).getDate();
        let row = calendarBody.insertRow();
        
        // Fill empty cells until first day of the month
        for (let i = 0; i < firstDay; i++) {
            row.insertCell();
        }

        // Fill the calendar with days and tasks
        for (let day = 1; day <= lastDate; day++) {
            let cell = row.insertCell();
            cell.innerHTML = day;

            const taskList = document.createElement('div');
            tasks.forEach(task => {
                let taskDate = new Date(task.taskDate);
                if (taskDate.getDate() === day && taskDate.getMonth() === month) {
                    let taskDiv = document.createElement('div');
                    taskDiv.className = 'task';
                    taskDiv.innerText = task.title;
                    taskList.appendChild(taskDiv);
                }
            });
            cell.appendChild(taskList);

            if ((day + firstDay) % 7 === 0) {
                row = calendarBody.insertRow();
            }
        }
    }

    createCalendar(new Date().getFullYear(), new Date().getMonth());
});
