$(document).ready(function () {
  // Load content in iframe on menu click
  $('.admp-tab').on('click', function (e) {
    e.preventDefault();
    var pageUrl = $(this).attr('href');
    $('.admp-mn-tfr').attr('src', pageUrl);
  });

  // Update date and time every second
  function updateDateTime() {
    const now = new Date();
    const date = now.toLocaleDateString('en-IN', {
      day: '2-digit',
      month: 'short',
      year: 'numeric'
    });
    const time = now.toLocaleTimeString('en-IN');
    $('#adp_dt').text(date);
    $('#adp_tm').text(time);
  }

  updateDateTime(); // initial call
  setInterval(updateDateTime, 1000); // update every second
});
