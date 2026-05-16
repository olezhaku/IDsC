const MONTH_NAMES = [
  "Jan",
  "Feb",
  "Mar",
  "Apr",
  "May",
  "Jun",
  "Jul",
  "Aug",
  "Sep",
  "Oct",
  "Nov",
  "Dec",
];
const WEEKDAY_NAMES = ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"];

const pad = (value: number) => String(value).padStart(2, "0");
const getRandomNumber = (max: number, random: () => number) =>
  Math.floor(random() * max);

const getBuildIdDatePart = (buildId: string) => {
  const [, datePart = ""] = buildId.split(".");

  if (!/^\d{6}$/.test(datePart)) {
    throw new Error(`Unsupported build id format: ${buildId}`);
  }

  return datePart;
};

export const generateBuildDate = (
  buildId: string,
  random: () => number = Math.random,
) => {
  const datePart = getBuildIdDatePart(buildId);
  const year = `20${datePart.slice(0, 2)}`;
  const month = datePart.slice(2, 4);
  const day = datePart.slice(4, 6);
  const hour = pad(getRandomNumber(24, random));
  const minute = pad(getRandomNumber(60, random));
  const second = pad(getRandomNumber(60, random));

  const date = new Date(
    Date.UTC(
      Number(year),
      Number(month) - 1,
      Number(day),
      Number(hour),
      Number(minute),
      Number(second),
    ),
  );

  return {
    year,
    month,
    day,
    hour,
    minute,
    second,
    buildDate: `${year}${month}${day}`,
    buildTime: `${hour}${minute}${second}`,
    buildDateStr: `${WEEKDAY_NAMES[date.getUTCDay()]} ${
      MONTH_NAMES[Number(month) - 1]
    } ${day} ${hour}:${minute}:${second} CST ${year}`,
    buildUtc: String(Math.floor(date.getTime() / 1000)),
    gitBuildTime: `${year}${month}${day}${hour}${minute}${second}`,
    securityPatch: `${year}-${month}-05`,
  };
};
