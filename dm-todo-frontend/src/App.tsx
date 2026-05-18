import { useEffect, useState } from 'react';
import { Routes, Route } from 'react-router-dom';
import { TaskDto, Status } from './types';
import { fetchTasks, createTask, updateTask } from './api';
import { TaskInput } from './components/TaskInput';
import { TaskList } from './components/TaskList';
import { TaskModal } from './components/TaskModal';

function App() {
  const [tasks, setTasks] = useState<TaskDto[]>([]);
  const [loading, setLoading] = useState(true);

  const loadTasks = async () => {
    try {
      const data = await fetchTasks();
      setTasks(data);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadTasks();
  }, []);

  const handleAdd = async (title: string) => {
    try {
      const newTask = await createTask({ title, status: Status.TODO });
      setTasks((prev) => [...prev, newTask]);
    } catch (err) {
      console.error(err);
    }
  };

  const handleStatusChange = async (task: TaskDto, newStatus: Status) => {
    try {
      const updatedTask = await updateTask(task.id!, { ...task, status: newStatus });
      setTasks((prev) => prev.map((t) => (t.id === updatedTask.id ? updatedTask : t)));
    } catch (err) {
      console.error(err);
    }
  };

  return (
    <div className="min-h-screen bg-gray-50 py-12 px-4 sm:px-6 lg:px-8">
      <div className="max-w-2xl mx-auto">
        <h1 className="text-3xl font-extrabold text-center text-gray-900 mb-8 tracking-tight">dm.de TODOs</h1>
        <TaskInput onAdd={handleAdd} />
        
        {loading ? (
          <div className="text-center py-12 text-gray-500">Loading...</div>
        ) : (
          <TaskList tasks={tasks} onStatusChange={handleStatusChange} />
        )}

        <Routes>
          <Route path="/task/:id" element={<TaskModal onTaskUpdated={loadTasks} />} />
        </Routes>
      </div>
    </div>
  );
}

export default App;
