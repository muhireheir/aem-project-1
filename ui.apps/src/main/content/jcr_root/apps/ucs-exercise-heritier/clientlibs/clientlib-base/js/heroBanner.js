$(document).ready(function () {
  var windowWidthHero = $(window).width();

  createHeroSlider();

  /****************************************************
   * CHART CONFIG
   ****************************************************/

  // 1) Data for doughnutChart & horizontalChart (only value)
  var labelsChart = [
    "Commercial Banking",
    "CEE Division",
    "Commercial Advice",
    "USA Division",
    "Corporate & Invest",
  ];
  var backgroundsChart = [
    "#01c0f3",
    "#abe0fa",
    "#94cb83",
    "#bbbdbf",
    "#6d6e71",
  ];
  var dataChart = [40, 15, 8, 64, 93];

  // 2) Data for lineChart (value & time)
  var dataValueChart = [
    {
      name: "IT Banking",
      value: [86, 3000, 106, 106, 300, 133, 500, 221, 783, 2478],
      color: "#01c0f3",
    },
    {
      name: "CEE Division",
      value: [282, 1981, 1000, 502, 635, 400, 1000, 1402, 3700, 4000],
      color: "#abe0fa",
    },
    {
      name: "RU Division",
      value: [168, 675, 178, 1402, 203, 408, 3000, 547, 675, 734],
      color: "#94cb83",
    },
    {
      name: "USA Division",
      value: [40, 200, 10, 16, 2478, 38, 2000, 167, 508, 784],
      color: "#bbbdbf",
    },
  ];
  var dataTimeChart = [2005, 2010, 2015, 2020];

  // 3) CHOISE CHART HERE ****
  doughnutChart($(".chartOne"), labelsChart, backgroundsChart, dataChart);
  horizontalChart($(".chartTwo"), labelsChart, backgroundsChart, dataChart);
  lineChart($(".chartThree"), dataValueChart, dataTimeChart);

  //RESIZE MANAGEMENT
  $(window).on("resize", function () {
    if ($(window).width() !== windowWidthHero) {
      windowWidthHero = $(window).width();
      createHeroSlider();
    }
  });
});

/****************************************************
 * FUNCTIONS
 ****************************************************/

/* CREATE SLIDER
  ============================= */

var createHeroSlider = function () {
  // destroy and initialize again
  if ($(".heroBanner .swiper-slide-active").length > 0) {
    $(".heroBanner__container")[0].swiper.destroy();
    addBg($(window).width());
  }

  var w = $(window).width();
  if (w > 1025) {
    heroBanneSwiper = new Swiper(".heroBanner__container", {
      loop: true,
      slidesPerGroup: 1,
      autoResize: true,
      simulateTouch: false,
      autoplay: {
        delay: 4000,
      },
      loopFillGroupWithBlank: true,
      slidesPerView: "auto",
      on: {
        transitionEnd: function () {
          var active = $(".heroBanner .swiper-slide-active").attr(
            "data-slide-number"
          );
          $(".heroBanner [data-number] .current").text(active);
        },
      },
      // Navigation arrows
      navigation: {
        nextEl: ".heroBanner [data-next-small]",
        prevEl: ".heroBanner [data-prev-small]",
      },
    });
  } else {
    heroBanneSwiper = new Swiper(".heroBanner__container", {
      spaceBetween: 10,
      autoplay: {
        delay: 4000,
      },
      paginationClickable: true,
      autoResize: true,
      pagination: {
        el: ".heroBanner [data-pagination]",
        clickable: true,
      },
    });
  }

  var active = $(".heroBanner .swiper-slide-active").attr("data-slide-number");
  var total = $(".heroBanner .swiper-slide:not(.swiper-slide-duplicate)")
    .length;
  $(".heroBanner [data-number] .current").text(active);
  $(".heroBanner [data-number] .total").text(total);
};

/* CREATE CHARTS FUNCTION
  ============================= */

function doughnutChart(slider, chartLabels, chartBg, chartData) {
  slider.find(".heroChart2").hide();
  slider.find(".heroChart3").hide();

  var myChart = slider.find(".heroChart");

  myChart.each(function () {
    chart = new Chart($(this), {
      type: "doughnut",
      animation: {
        animateScale: true,
      },
      data: {
        labels: chartLabels,
        datasets: [
          {
            label: "unicredit-data",
            data: chartData,
            backgroundColor: chartBg,
            borderColor: "#262626",
            borderWidth: 1,
          },
        ],
      },
      options: {
        responsive: true,
        legend: false,
        cutoutPercentage: 70,
        legendCallback: function (chart) {
          var legendHtml = [];
          legendHtml.push("<ul>");
          var item = chart.data.datasets[0];
          for (var i = 0; i < item.data.length; i++) {
            legendHtml.push("<li>");
            legendHtml.push(
              '<span class="chart-legend" style="background-color:' +
                item.backgroundColor[i] +
                '"></span>'
            );
            legendHtml.push(
              '<span class="chart-legend-label-text">' +
                chart.data.labels[i] +
                "</span>" +
                '<span class="chart-legend-label-data">' +
                item.data[i] +
                "</span>" +
                '<span class="chart-legend-um">' +
                "%" +
                "</span>"
            );
            legendHtml.push("</li>");
          }

          legendHtml.push("</ul>");
          return legendHtml.join("");
        },
        tooltips: {
          enabled: false,
        },
      },
    });
  });
  slider.find(".legend-con").html(chart.generateLegend());
}

function horizontalChart(slider, chartLabels, chartBg, chartData) {
  slider.find(".heroChart").hide();
  slider.find(".heroChart3").hide();

  slider.find(".legend-con").css("margin-left", "0");
  if ($(window).width() <= 1024) {
    slider.find(".heroChart2").css("width", "100%");
    slider.find(".canvas-con").css("padding", "0 10px");
  }
  var myChart = slider.find(".heroChart2");

  myChart.each(function () {
    chart = new Chart($(this), {
      type: "horizontalBar",
      animation: {
        animateScale: true,
      },
      data: {
        labels: chartLabels,
        datasets: [
          {
            label: " Value: ",
            data: chartData,
            backgroundColor: chartBg,
          },
        ],
      },
      options: {
        // responsive: true,
        maintainAspectRatio: false,
        legend: false,
        title: {
          display: false,
          text: "",
        },
        scales: {
          yAxes: [
            {
              ticks: {
                fontColor: "white",
              },
              gridLines: {
                color: "rgba(100, 100, 100, 0.1)",
              },
            },
          ],
          xAxes: [
            {
              ticks: {
                fontColor: "white",
                min: 0,
                // max: 100,
                stepSize: 20,
                beginAtZero: true,
                callback: function (value) {
                  return value + "%";
                },
              },
              gridLines: {
                color: "rgba(100, 100, 100, 0.1)",
              },
            },
          ],
        },
      },
    });
  });
}

function lineChart(slider, dataValue, dataTime) {
  slider.find(".heroChart").hide();
  slider.find(".heroChart2").hide();

  slider.find(".legend-con").css("margin-left", "0");
  if ($(window).width() <= 1024) {
    slider.find(".heroChart3").css("width", "100%");
    slider.find(".canvas-con").css("padding", "0 10px");
  }
  var myChart = slider.find(".heroChart3");

  var dataForChart = [];
  dataValue.map(function (item) {
    dataForChart.push({
      data: item.value,
      label: item.name,
      borderColor: item.color,
      fill: false,
    });
  });

  myChart.each(function () {
    chart = new Chart($(this), {
      type: "line",
      animation: {
        animateScale: true,
      },
      data: {
        labels: dataTime,
        datasets: dataForChart,
      },
      options: {
        maintainAspectRatio: false,
        legend: {
          labels: {
            padding: 15,
            fontColor: "white",
          },
        },
        title: {
          display: false,
          text: "",
        },
        scales: {
          yAxes: [
            {
              ticks: {
                fontColor: "white",
                stepSize: 1000,
              },
              gridLines: {
                color: "rgba(100, 100, 100, 0.1)",
              },
            },
          ],
          xAxes: [
            {
              ticks: {
                fontColor: "white",
                min: 0,
                // max: 100,
                beginAtZero: true,
              },
              gridLines: {
                color: "rgba(100, 100, 100, 0.1)",
              },
            },
          ],
        },
      },
    });
  });
}
