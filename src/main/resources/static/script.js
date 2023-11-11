function takeStudentData() {
    var numStudents = document.getElementById("numStudents").value;
    var studentDataContainer = document.getElementById("studentDataContainer");

    var form = document.createElement("form");
    form.setAttribute("id", "studentDataForm");

    for (var i = 0; i < numStudents; i++) {
        var div = document.createElement("div");

        div.innerHTML = `
            <label for="studentID">Student ID:</label>
            <input type="text" id="studentID" name="studentID" required>

            <label for="firstName">First Name:</label>
            <input type="text" id="firstName" name="firstName" required>

            <label for="lastName">Last Name:</label>
            <input type="text" id="lastName" name="lastName" required>

            <label for="gender">Gender:</label>
            <input type="text" id="gender" name="gender" required>

            <label for="gpa">GPA:</label>
            <input type="text" id="gpa" name="gpa" required>

            <label for="level">Level:</label>
            <input type="text" id="level" name="level" required>

            <label for="address">Address:</label>
            <input type="text" id="address" name="address" required>
        `;

        form.appendChild(div);
    }

    form.innerHTML += `<button type="button" onclick="storeData()">Store Data</button>`;
    studentDataContainer.innerHTML = "";
    studentDataContainer.appendChild(form);
}

function storeData() {
    var form = document.getElementById("studentDataForm");
    var formData = {};

    // Collect form data into a JavaScript object
    for (var i = 0; i < form.elements.length; i++) {
        var element = form.elements[i];
        if (element.name) {
            formData[i] = element.value;
        }

    }
    fetch('/storeData', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(formData),
    })
        .then(response => {
            if (!response.ok) {
                alert('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            alert('Data stored successfully:', data);
            // Optionally, you can update the UI or perform other actions on success
        })
        .catch(error => {
            alert('Error storing Student data:', error);
            // Handle errors, update the UI, or perform other actions on failure
        });

    // Send a POST request with JSON data

}

function search() {
    var searchInput = document.getElementById("searchInput").value;
    var searchBy = document.getElementById("searchBy").value;

    fetch(`/search?keyword=${searchInput}&searchBy=${searchBy}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
        },
    })
        .then(response => {
            if (!response.ok) {
                alert('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            displaySearchResults(data);
        })
        .catch(error => {
            alert('Error searching:', error);
            // Handle errors, update the UI, or perform other actions on failure
        });
}

function displaySearchResults(results) {
    var resultsContainer = document.getElementById("searchResultsContainer");

    // Clear previous results
    resultsContainer.innerHTML = "";

    if (results.length === 0) {
        resultsContainer.innerHTML = "No results found.";
        return;
    }

    var table = document.createElement("table");
    table.classList.add("results-table");

    // Create table header
    var headerRow = table.insertRow(0);
    for (var key in results[0]) {
        var th = document.createElement("th");
        th.textContent = key;
        headerRow.appendChild(th);
    }

    // Create table rows with data
    for (var i = 0; i < results.length; i++) {
        var row = table.insertRow(i + 1);
        for (var key in results[i]) {
            var cell = row.insertCell();
            cell.textContent = results[i][key];
        }
    }

    resultsContainer.appendChild(table);
}

function deleteStudent(message) {
    var studentID = document.getElementById("deleteInput").value;

    if (!studentID) {
        alert('Student ID is required for deletion');
        return;
    }

    fetch(`/delete?studentId=${studentID}`, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json',
        },
    })
        .then(response => {
            if (!response.ok) {
                alert('Network response was not ok');
            }
            alert("Student deleted successfully");
        })

        .catch(error => {
            alert('Error deleting student:', error);
            // Handle errors, update the UI, or perform other actions on failure
        });
}