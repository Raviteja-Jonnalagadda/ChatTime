$(document).ready(function () {
    const $dateEl = $("#date");
    const $timeEl = $("#time");
    const $sendBtn = $("#sendBtn");
    const $msgInput = $("#msgInput");
    const $messages = $("#messages");

    // Update date & time
    function updateDateTime() {
        const now = new Date();
        $dateEl.text("Date: " + now.toLocaleDateString());
        $timeEl.text("Time: " + now.toLocaleTimeString());
    }
    setInterval(updateDateTime, 1000);
    updateDateTime();

    // Send message
    $sendBtn.on("click", function () {
        const msg = $msgInput.val().trim();
        if (msg) {
            const $div = $("<div>")
                .addClass("message sent")
                .text(msg);
            $messages.append($div);
            $msgInput.val("");
            $messages.scrollTop($messages[0].scrollHeight);
        }
    });

    // Enter key support
    $msgInput.on("keypress", function (e) {
        if (e.which === 13) { // 13 = Enter key
            $sendBtn.click();
        }
    });
});
