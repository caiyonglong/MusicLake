package com.cyl.musiclake.ui.download.ui

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView

import com.cyl.musiclake.MusicApp
import com.cyl.musiclake.MusicApp.mContext
import com.cyl.musiclake.R
import com.cyl.musiclake.ui.download.TasksManager
import com.cyl.musiclake.ui.download.TasksManagerModel
import com.cyl.musiclake.utils.LogUtil
import com.liulishuo.filedownloader.BaseDownloadTask
import com.liulishuo.filedownloader.FileDownloader
import com.liulishuo.filedownloader.model.FileDownloadStatus
import com.liulishuo.filedownloader.util.FileDownloadUtils

import java.io.File

/**
 * Created by yonglong on 2018/1/23.
 */

class TaskItemAdapter(private val mContext: Context, var models: List<TasksManagerModel>?) : RecyclerView.Adapter<TaskItemAdapter.TaskItemViewHolder>() {

    private val taskActionOnClickListener = View.OnClickListener { v ->
        if (v.tag == null) {
            return@OnClickListener
        }
        val holder = v.tag as TaskItemViewHolder
        when ((v as TextView).text) {
            v.getResources().getString(R.string.pause) -> // to pause
                FileDownloader.getImpl().pause(holder.id)
            v.getResources().getString(R.string.start) -> {
                // to start
                val model = TasksManager[holder.adapterPosition]
                val task = FileDownloader.getImpl().create(model.url)
                        .setPath(model.path)
                        .setCallbackProgressTimes(100)
                        .setListener(taskDownloadListener)
                TasksManager.addTaskForViewHolder(task)
                holder.id = task.id
                TasksManager.updateViewHolder(holder.id, holder)

                task.start()
            }
            v.getResources().getString(R.string.delete) -> {
                // to delete
                File(TasksManager[holder.adapterPosition].path).delete()
                holder.taskActionBtn.isEnabled = true
                holder.updateNotDownloaded(FileDownloadStatus.INVALID_STATUS.toInt(), 0, 0)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskItemViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_download_music, parent, false)
        return TaskItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskItemViewHolder, position: Int) {
        val model = models!![position]
        holder.taskActionBtn.setOnClickListener(taskActionOnClickListener)
        holder.update(model.tid, holder.adapterPosition)
        holder.taskActionBtn.tag = holder
        holder.taskNameTv.text = model.name

        TasksManager.updateViewHolder(holder.id, holder)

        holder.taskActionBtn.isEnabled = true
        if (FileDownloader.getImpl().isServiceConnected) {
            val status = TasksManager.getStatus(model.tid, model.path!!)
            if (status == FileDownloadStatus.pending.toInt() || status == FileDownloadStatus.started.toInt() ||
                    status == FileDownloadStatus.connected.toInt()) {
                // start task, but file not created yet
                holder.updateDownloading(status, TasksManager.getSoFar(model.tid), TasksManager.getTotal(model.tid))
            } else if (!File(model.path).exists() && !File(FileDownloadUtils.getTempPath(model.path)).exists()) {
                // not exist file
                holder.updateNotDownloaded(status, 0, 0)
            } else if (TasksManager.isDownloaded(status)) {
                // already downloaded and exist
                LogUtil.e(TAG, "already downloaded and exist")
                holder.updateDownloaded()
            } else if (status == FileDownloadStatus.progress.toInt()) {
                // downloading
                holder.updateDownloading(status, TasksManager.getSoFar(model.tid), TasksManager.getTotal(model.tid))
            } else {
                // not start
                holder.updateNotDownloaded(status, TasksManager.getSoFar(model.tid), TasksManager.getTotal(model.tid))
            }
        } else {
            holder.taskStatusTv.setText(R.string.tasks_manager_demo_status_loading)
            holder.taskActionBtn.isEnabled = false
        }
    }

    override fun getItemCount(): Int {
        return models!!.size
    }

    inner class TaskItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var taskNameTv = itemView.findViewById<TextView>(R.id.task_name_tv)
        var taskStatusTv = itemView.findViewById<TextView>(R.id.task_status_tv)
        var taskPb = itemView.findViewById<ProgressBar>(R.id.task_pb)
        var taskActionBtn = itemView.findViewById<Button>(R.id.task_action_btn)

        /**
         * viewHolder position
         */
        var position1 = 0
        /**
         * download id
         */
        var id: Int = 0

//        internal var taskNameTv: TextView
//        internal var taskStatusTv: TextView
//        internal var taskPb: ProgressBar
//        internal var taskActionBtn: Button

        fun update(id: Int, position: Int) {
            this.id = id
            this.position1 = position
        }

        fun updateDownloaded() {
            taskPb.max = 1
            taskPb.progress = 1
            taskStatusTv.setText(R.string.tasks_manager_demo_status_completed)
            taskActionBtn.setText(R.string.delete)
            taskActionBtn.visibility = View.GONE
            taskPb.visibility = View.GONE
            TasksManager.finishTask(id)
        }

        fun updateNotDownloaded(status: Int, sofar: Long, total: Long) {
            if (sofar > 0 && total > 0) {
                val percent = sofar / total.toFloat()
                taskPb.max = 100
                taskPb.progress = (percent * 100).toInt()
            } else {
                taskPb.max = 1
                taskPb.progress = 0
            }

            when (status.toByte()) {
                FileDownloadStatus.error -> taskStatusTv.setText(R.string.tasks_manager_demo_status_error)
                FileDownloadStatus.paused -> taskStatusTv.setText(R.string.tasks_manager_demo_status_paused)
                else -> taskStatusTv.setText(R.string.tasks_manager_demo_status_not_downloaded)
            }
            taskActionBtn.setText(R.string.start)
        }

        fun updateDownloading(status: Int, sofar: Long, total: Long) {
            val percent = sofar / total.toFloat()
            taskPb.max = 100
            taskPb.progress = (percent * 100).toInt()

            when (status.toByte()) {
                FileDownloadStatus.pending -> taskStatusTv.setText(R.string.tasks_manager_demo_status_pending)
                FileDownloadStatus.started -> taskStatusTv.setText(R.string.tasks_manager_demo_status_started)
                FileDownloadStatus.connected -> taskStatusTv.setText(R.string.tasks_manager_demo_status_connected)
//                FileDownloadStatus.progress -> taskStatusTv.setText(R.string.tasks_manager_demo_status_progress)
                else -> taskStatusTv.text = MusicApp.mContext.getString(
                        R.string.tasks_manager_demo_status_downloading, (percent * 100).toInt())
            }

            taskActionBtn.setText(R.string.pause)
        }
    }

    companion object {
        private val TAG = "TaskItemAdapter"

        val taskDownloadListener = FileDownloadListener()
    }
}

