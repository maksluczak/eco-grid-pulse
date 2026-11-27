import { useState, type FormEvent } from "react";
import { useSubmit } from "../../hooks/useSubmit";
import type { AuthRequest } from "./types";

interface AuthFormProps {
    title: string;
    submitLabel: string;
    onSubmit: (data: AuthRequest) => Promise<void>;
}

export default function AuthForm({ title, submitLabel, onSubmit }: AuthFormProps) {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const { submit, error, isSubmitting } = useSubmit();

    const handleSubmit = (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        submit(() => onSubmit({ email, password }));
    };

    return (
        <form onSubmit={handleSubmit}>
            <h2>{title}</h2>
            <label>
                Email
                <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} required />
            </label>
            <label>
                Password
                <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} required />
            </label>
            {error && <p role="alert">{error}</p>}
            <button type="submit" disabled={isSubmitting}>
                {isSubmitting ? "Sending..." : submitLabel}
            </button>
        </form>
    );
}
