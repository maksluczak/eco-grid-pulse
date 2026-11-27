interface DurationSelectProps {
    value: number;
    onChange: (hours: number) => void;
    maxHours?: number;
}

export default function DurationSelect({ value, onChange, maxHours = 6 }: DurationSelectProps) {
    const hours = Array.from({ length: maxHours }, (_, index) => index + 1);

    return (
        <label>
            Charging time
            <select value={value} onChange={(e) => onChange(Number(e.target.value))}>
                {hours.map((hour) => (
                    <option key={hour} value={hour}>{hour} h</option>
                ))}
            </select>
        </label>
    );
}
