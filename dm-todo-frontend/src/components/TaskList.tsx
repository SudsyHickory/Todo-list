import { TaskDto, Status } from '../types';
import { TaskItem } from './TaskItem';

interface TaskListProps {
  tasks: TaskDto[];
  onStatusChange: (task: TaskDto, newStatus: Status) => void;
}

export function TaskList({ tasks, onStatusChange }: TaskListProps) {
  if (tasks.length === 0) {
    return (
      <div className="text-center py-12 bg-white rounded-lg shadow-sm border border-gray-100 text-gray-500">
        No tasks yet. Add one above!
      </div>
    );
  }

  // Sort tasks: TODO first, then DONE
  const sortedTasks = [...tasks].sort((a, b) => {
    if (a.status === b.status) return 0;
    return a.status === Status.TODO ? -1 : 1;
  });

  return (
    <div className="flex flex-col gap-3">
      {sortedTasks.map((task) => (
        <TaskItem
          key={task.id}
          task={task}
          onStatusChange={onStatusChange}
        />
      ))}
    </div>
  );
}
