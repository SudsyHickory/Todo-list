export enum Status {
    TODO = 'TODO',
    DONE = 'DONE'
}

export interface TaskDto {
    id?: number;
    title: string;
    description?: string;
    status: Status;
    version?: number
}
