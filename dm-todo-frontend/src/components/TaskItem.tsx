import { TaskDto, Status } from '../types';
import { useNavigate } from 'react-router-dom';

interface TaskItemProps {
  task: TaskDto;
  onStatusChange: (task: TaskDto, newStatus: Status) => void;
}

export function TaskItem({ task, onStatusChange }: TaskItemProps) {
  const navigate = useNavigate();

  const handleCheckboxChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    e.stopPropagation();
    const newStatus = e.target.checked ? Status.DONE : Status.TODO;
    onStatusChange(task, newStatus);
  };

  const handleItemClick = () => {
    navigate(`/task/${task.id}`);
  };

  const isDone = task.status === Status.DONE;

  return (
    <div
      onClick={handleItemClick}
      className={`flex items-center gap-4 bg-white p-4 rounded-md shadow-sm border border-gray-100 cursor-pointer hover:shadow-md transition-shadow ${
        isDone ? 'opacity-70' : ''
      }`}
    >
      <input
        type="checkbox"
        className="w-5 h-5 text-blue-600 rounded border-gray-300 focus:ring-blue-500 cursor-pointer"
        checked={isDone}
        onChange={handleCheckboxChange}
        onClick={(e) => e.stopPropagation()}
      />
      <div className={`flex-1 text-lg ${isDone ? 'line-through text-gray-500' : 'text-gray-800'}`}>
        {task.title}
      </div>
    </div>
  );
}
