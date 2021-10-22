$(document).ready(() => {
    $(document).ajaxSend(function() {
        $("#overlay").fadeIn(300);
    });
    $("#download-form").on("submit", (e) => {
        e.preventDefault();
        let form = document.getElementById("download-form");
        $.ajax({
            type: "POST",
            url: "/api/prepare",
            contentType: "application/json",
            data: JSON.stringify(Form2JsonMapper(form)),
            success: (resp) => {
                window.location="/api/download?fileName="+resp
                Swal.fire(
                    'Your file!',
                    '<p class="alert alert-info alert-link">Downloaded!</p>',
                    'success'
                );

                $("#overlay").fadeOut(300);
            }, error: (error) => {
                Swal.fire({
                    icon: 'error',
                    title: 'Oops..',
                    html: getErrorMessage(error),
                });

                $("#overlay").fadeOut(300);
            }
        })
    });
});

function Form2JsonMapper(form) {
    let formData = form.querySelectorAll("input, select, textarea");
    let obj = {};
    formData.forEach(e => {
        let name = e.name;
        let value = e.value;
        if (name) {
            obj[name] = value;
        }
    });
    return obj;
}

function generateFormArray(elements) {
    let listElement = [];
    elements.forEach(form => {
        listElement.push(Form2JsonMapper(form));
    });
    return listElement;
}

function getErrorMessage(resp) {
    let html;
    $.each(resp.responseJSON, (key, value)=>{
        html += '<div class="alert alert-danger">'+key.toUpperCase() + ': ' + value+'</div>';
    });
    return html;
}