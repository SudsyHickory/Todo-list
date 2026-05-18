import { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { TaskDto } from '../types';
import { fetchTask, updateTask, deleteTask } from '../api';

interface TaskModalProps {
  onTaskUpdated: () => void;
}

export function TaskModal({ onTaskUpdated }: TaskModalProps) {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const [task, setTask] = useState<TaskDto | null>(null);
  const [loading, setLoading] = useState(true);

  // Form states
  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');

  useEffect(() => {
    if (!id) return;
    
    fetchTask(Number(id))
      .then(fetchedTask => {
        setTask(fetchedTask);
        setTitle(fetchedTask.title);
        setDescription(fetchedTask.description || '');
        setLoading(false);
      })
      .catch(err => {
        console.error(err);
        navigate('/');
      });
  }, [id, navigate]);

  const handleClose = () => {
    navigate('/');
  };

  const handleSave = async () => {
    if (!task) return;
    try {
      await updateTask(task.id!, {
        ...task,
        title,
        description
      });
      onTaskUpdated();
      handleClose();
    } catch (err) {
      console.error(err);
    }
  };

  const handleDelete = async () => {
    if (!task || !task.id) return;
    try {
      await deleteTask(task.id);
      onTaskUpdated();
      handleClose();
    } catch (err) {
      console.error(err);
    }
  };

  const handleBackdropClick = (e: React.MouseEvent) => {
    if (e.target === e.currentTarget) {
      handleClose();
    }
  };

  if (loading) {
    return null;
  }

  return (
    <div
      className="fixed inset-0 bg-black/50 flex justify-center items-center p-4 z-50 transition-opacity"
      onClick={handleBackdropClick}
    >
      <div className="bg-white rounded-lg shadow-xl w-full max-w-md max-h-[90vh] flex flex-col transform transition-all">
        <div className="p-6 flex-1 overflow-y-auto">
          <div className="mb-4">
            <label className="block text-sm font-medium text-gray-700 mb-1">Title</label>
            <input
              type="text"
              className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 font-semibold"
              value={title}
              onChange={(e) => setTitle(e.target.value)}
              autoFocus
            />
          </div>
          <div className="mb-4">
            <label className="block text-sm font-medium text-gray-700 mb-1">Description</label>
            <textarea
              className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 min-h-[120px] resize-y"
              value={description}
              onChange={(e) => setDescription(e.target.value)}
            />
          </div>
        </div>
        <div className="bg-gray-50 px-6 py-4 rounded-b-lg flex justify-between items-center gap-3">
          <button
            onClick={handleDelete}
            className="px-4 py-2 text-red-600 bg-red-50 hover:bg-red-100 rounded-md font-medium transition-colors"
          >
            Delete Task
          </button>
          <div className="flex gap-3">
            <button
              onClick={handleClose}
              className="px-4 py-2 text-gray-700 bg-white border border-gray-300 hover:bg-gray-50 rounded-md font-medium transition-colors"
            >
              Cancel
            </button>
            <button
              onClick={handleSave}
              className="px-4 py-2 text-white bg-blue-600 hover:bg-blue-700 rounded-md font-medium transition-colors shadow-sm"
            >
              Save
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}
