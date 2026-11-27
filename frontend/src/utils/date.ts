export const getDaysInMonth = (year: number, month: number) => {
    return new Date(year, month + 1, 0).getDate();
};

export const getFirstWeekdayOfMonth = (year: number, month: number) => {
    return (new Date(year, month, 1).getDay() + 6) % 7;
};

export const isSameDay = (first: Date, second: Date) => {
    return (
        first.getDate() === second.getDate() &&
        first.getMonth() === second.getMonth() &&
        first.getFullYear() === second.getFullYear()
    );
};

export const formatDateForBackend = (date: Date) => {
    const day = String(date.getDate()).padStart(2, "0");
    const month = String(date.getMonth() + 1).padStart(2, "0");
    const year = String(date.getFullYear());

    return year + "-" + month + "-" + day;
};

export const formatDateTime = (value: string) => {
    const [date, time] = value.split("T");
    const [year, month, day] = date.split("-");

    return `${day}/${month}/${year} ${time.slice(0, 5)}`;
}

export const formatTime = (value: string) => {
    return value.split("T")[1].slice(0, 5);
}
