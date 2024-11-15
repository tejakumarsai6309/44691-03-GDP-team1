package com.GDP.TaskMasterDemo.Model;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Long id;
    @NotEmpty(message = "{task.name.not.empty}")
    private String name;
    @NotEmpty(message = "{task.description.not.empty}")
    @Column(length = 1200)
    @Size(max = 1200, message = "{task.description.size}")
    private String description;
    
    @NotNull(message = "{task.date.not.null}")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime  date;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime  creationDateTime;
    
    private boolean isCompleted;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime  completeDateTime;
    
    private String taskPriority;
    
    private String creatorName;
    
    @ManyToOne
    @JoinColumn(name = "OWNER_ID")
    private User owner;
    
    @OneToMany(mappedBy = "task", cascade = CascadeType.MERGE, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    // Getters and Setters
    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
    
    public void addComment(Comment comment) {
        this.comments.add(comment);
    }
    
    public long daysLeftUntilDeadline(LocalDateTime date) {
        return ChronoUnit.DAYS.between(LocalDateTime.now(), date);
    }

    public Task() {
    }
    
    public Task(@NotEmpty String name,
            @NotEmpty @Size(max = 1200) String description,
            @NotNull LocalDateTime  date,@NotNull LocalDateTime  completeDateTime,
            boolean isCompleted,String creatorName) {
    this.name = name;
    this.description = description;
    this.date = date;
    this.completeDateTime = completeDateTime;
    this.isCompleted = isCompleted;
    this.creatorName = creatorName;
    }
    
    public Task(@NotEmpty String name,
            @NotEmpty @Size(max = 1200) String description,
            @NotNull LocalDateTime  date,@NotNull LocalDateTime completeDateTime,
            boolean isCompleted,String creatorName,
            String taskPriority ) {
    this.name = name;
    this.description = description;
    this.date = date;
    this.completeDateTime = completeDateTime;
    this.isCompleted = isCompleted;
    this.creatorName = creatorName;
    this.taskPriority = taskPriority;
    }
    
    
    public Task(@NotEmpty String name,
            @NotEmpty @Size(max = 1200) String description,
            @NotNull LocalDateTime  date,@NotNull LocalDateTime completeDateTime,
            boolean isCompleted,String creatorName,
            User owner,String taskPriority ) {
    this.name = name;
    this.description = description;
    this.date = date;
    this.completeDateTime = completeDateTime;
    this.isCompleted = isCompleted;
    this.creatorName = creatorName;
    this.owner = owner;
    this.taskPriority = taskPriority;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime  getDate() {
        return date;
    }

    public void setDate(LocalDateTime  date) {
        this.date = date;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }   
    

	public String getTaskPriority() {
		return taskPriority;
	}

	public void setTaskPriority(String taskPriority) {
		this.taskPriority = taskPriority;
	}
	
	public LocalDateTime getCompleteDateTime() {
		return completeDateTime;
	}

	public void setCompleteDateTime(LocalDateTime completeDateTime) {
		this.completeDateTime = completeDateTime;
	}

	public LocalDateTime getCreationDateTime() {
		return creationDateTime;
	}

	public void setCreationDateTime(LocalDateTime creationDateTime) {
		this.creationDateTime = creationDateTime;
	}

	@Override
	public String toString() {
		return "Task [id=" + id + ", name=" + name + ", description=" + description + ", date=" + date
				+ ", isCompleted=" + isCompleted + ", taskPriority=" + taskPriority +
				", completeDateTime =" +completeDateTime + ",creationDateTime = "+creationDateTime
				+ ", creatorName=" + creatorName + ", owner=" + owner + "]";
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return isCompleted == task.isCompleted &&
                Objects.equals(id, task.id) &&
                name.equals(task.name) &&
                description.equals(task.description) &&
                date.equals(task.date) &&
                creationDateTime.equals(task.creationDateTime) &&
                Objects.equals(creatorName, task.creatorName) &&
                Objects.equals(owner, task.owner) &&
                completeDateTime.equals(task.completeDateTime) &&
                taskPriority.equals(task.taskPriority);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, date, creationDateTime, isCompleted, creatorName,completeDateTime, owner, taskPriority);
    }
}
