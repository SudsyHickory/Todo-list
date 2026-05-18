import { TaskDto } from './types';

const API_URL = 'http://localhost:8080/tasks';

export async function fetchTasks(): Promise<TaskDto[]> {
    const response = await fetch(API_URL);
    if (!response.ok) throw new Error('Failed to fetch tasks');
    return response.json();
}

export async function fetchTask(id: number): Promise<TaskDto> {
    const response = await fetch(`${API_URL}/${id}`);
    if (!response.ok) throw new Error('Failed to fetch task');
    return response.json();
}

export async function createTask(task: TaskDto): Promise<TaskDto> {
    const response = await fetch(API_URL, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(task),
    });
    if (!response.ok) throw new Error('Failed to create task');
    return response.json();
}

export async function updateTask(id: number, task: TaskDto): Promise<TaskDto> {
    const response = await fetch(`${API_URL}/${id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(task),
    });
    if (!response.ok) throw new Error('Failed to update task');
    return response.json();
}

export async function deleteTask(id: number): Promise<void> {
    const response = await fetch(`${API_URL}/${id}`, {
        method: 'DELETE',
    });
    if (!response.ok) throw new Error('Failed to delete task');
}
