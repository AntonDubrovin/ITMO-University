window.notify = function (message) {
    $.notify(message, {
        position: "right bottom",
        className: "success"
    });
}

ajax = function (data, $form) {
    const $error = $form.find(".error");

    $.ajax({
        type: "POST",
        dataType: "json",
        data,
        success: function (response) {
            if (response["error"]) {
                $error.text(response["error"]);
            } else {
                location.href = response["redirect"];
            }
        }
    })

}

