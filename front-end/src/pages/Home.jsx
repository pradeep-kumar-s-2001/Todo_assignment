import { ErrorMessage, Form, Formik } from "formik";
import React, { useEffect, useState } from "react";
import { CiLogout, CiCircleInfo, CiEdit, CiTrash } from "react-icons/ci";
import { MdAddTask } from "react-icons/md";
import { axiosInstance } from "../utils/axiosInstance";
import { addTaskValidation, getErrorMessage } from "../utils/formikValidation";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import toast from "react-hot-toast";
import Loader from "../components/Loader";
import { useNavigate } from "react-router-dom";

const Home = () => {
  const [addModal, setAddModal] = useState(false);
  const queryClient = useQueryClient();

  const [summary,setSummary] = useState("")

  const navigate = useNavigate();

  useEffect(()=>{
    
    if(!localStorage.getItem("todo-token"))
    {
      navigate("/signin")
    }

  },[])

  const handleLogout = () => {
    localStorage.removeItem("todo-token");
    toast.success("Account logged out successfully");
    setTimeout(() => {
      navigate("/signin");
    }, 1000);
  };

  const getSummary = async (val) => {
    const response = await axiosInstance.get("/todo/secure/get-summary");
    return response.data;
  };


  const getSummaryMutation = useMutation({
    mutationFn: getSummary,
    mutationKey: "task-summary",
    onSuccess: (result) => {
      toast.success("Summary generated and pushed to slack successfully");
      const {data} = result;
      setSummary(data);

    },
    onError: (error) => {
      toast.error(getErrorMessage(error));
    },
  });

  const addTask = async (val) => {
    const response = await axiosInstance.post("/todo/secure/create", val);
    return response.data;
  };

  const addTaskMutation = useMutation({
    mutationFn: addTask,
    mutationKey: "add-task",
    onSuccess: (result) => {
      toast.success("Task Added Successfully");

      queryClient.invalidateQueries(["all-todo"]);

      setTimeout(() => {
        setAddModal(false);
      }, 800);
    },
    onError: (error) => {
      toast.error(getErrorMessage(error));
    },
  });

  const getAllTodo = async () => {
    const response = await axiosInstance.get("/todo/secure/all");
    return response.data;
  };

  const { data: todoData, isLoading } = useQuery({
    queryKey: ["all-todo"],
    queryFn: getAllTodo,
  });

  let TODO_LIST = todoData?.data?.todoData;

  if (isLoading) {
    return (
      <section className="w-full h-[90vh] flex justify-center items-center">
        <span>Loading</span>
      </section>
    );
  }

  return (
    <>
      <section className="w-full min-h-screen p-5 font-outfit">
        <article className="w-full flex justify-between items-center">
          <span className="text-[1.5rem] text-blue-400 font-semibold relative flex justify-center items-center gap-2">
            Todo Summary Assitant{" "}
            <img
              src="https://icones.pro/wp-content/uploads/2022/10/icone-robot-blue.png"
              className="w-[60px] h-auto"
              alt=""
            />
          </span>

          <span
            className="p-2 bg-blue-400 rounded-md flex justify-center items-center gap-1 cursor-pointer"
            onClick={handleLogout}>
            <CiLogout /> Logout
          </span>
        </article>

        <section className="w-full flex flex-col-reverse md:flex-row justify-start gap-2 items-start mt-[50px]">
          <section className="w-full md:w-[70%] rounded-2xl p-5 shadow">
            <article className="w-full flex justify-end items-center">
              <button
                className="bg-blue-400 text-black px-2 py-1 rounded-2xl cursor-pointer"
                onClick={() => setAddModal(true)}
              >
                <MdAddTask size={16} className="inline" /> Add Task
              </button>
            </article>

            {TODO_LIST?.length === 0 ? (
              <section className="w-full h-[70vh] flex flex-col justify-center items-center">
                <img
                  src="https://png.pngtree.com/png-clipart/20250314/original/pngtree-cartoon-dog-in-distress-png-image_19332909.png"
                  className="w-[60px] h-auto"
                  alt=""
                />

                <p className="text-[0.8rem] text-gray-700">
                  No tasks found. Please add tasks to generate a summary.
                </p>
              </section>
            ) : (
              <section className="w-full min-h-[50vh] grid grid-cols-1  gap-3 mt-[30px]">
                {[...TODO_LIST].map((item, idx) => (
                  <TODO_CARD key={idx} data={item} />
                ))}
              </section>
            )}
          </section>

          <section className="w-full md:w-[30%] min-h-[50vh] shadow-md rounded-2xl">
            <h1 className=" text-[1.4rem] bg-blue-400 text-white-500 font-semibold text-center py-3 rounded-tl-2xl rounded-tr-2xl">
              Summary Overview
            </h1>

            {
                summary ==="" ? <article className="w-full h-[40vh] flex justify-center flex-col items-center">
                {
                    getSummaryMutation.isPending && <span className="flex flex-col justify-center items-center mb-[20px]"> <Loader />
                     
                     <span>Please wait while generating summary</span>
                    </span>
                }    
              <button className="bg-blue-400 text-black text-[0.95rem] cursor-pointer rounded-lg py-2 px-3" onClick={()=>getSummaryMutation.mutate()}>
                Get Summary and Push To stack
              </button>
              <p className="text-[0.8rem] text-gray-700 text-center w-[80%] mx-auto mt-[10px]">
                {" "}
                <CiCircleInfo size={16} className="inline" /> Generate a clear
                and concise summary of all your tasks, including their current
                status, progress, and key details.
              </p>
            </article> : 
            <article className="w-full h-[40vh] flex justify-center flex-col items-center relative">
              
              <p className="text-[0.9rem] text-black text-center w-[80%] mx-auto mt-[10px]">
                {summary}
              </p>


              <span className="absolute top-1 right-1 cursor-pointer text-[0.8rem]" onClick={()=>setSummary("")}>Close</span>

            </article>
            }
          </section>
        </section>
      </section>

      {/* Add Task Modal   */}
      {addModal && (
        <section
          className="fixed top-0 left-0 bg-black/60 w-full h-screen flex justify-center items-center font-outfit"
          onClick={() => setAddModal(false)}
        >
          <article
            className="p-3 bg-white rounded-2xl w-full max-w-[450px]"
            onClick={(e) => e.stopPropagation()}
          >
            <h1 className="text-2xl">Create Task</h1>

            <Formik
              initialValues={{ title: "" }}
              validationSchema={addTaskValidation}
              onSubmit={(val) => {
                addTaskMutation.mutate(val);
              }}
            >
              {({ handleBlur, handleChange }) => (
                <Form className="mt-[20px] w-full">
                  <article className="w-full">
                    <div className="w-full p-2 flex justify-start items-center border border-slate-200 rounded-xl">
                      {/* <CiMail size={18} /> */}
                      <textarea
                        rows={4}
                        type="text"
                        name="title"
                        placeholder="Enter task"
                        className="flex-1 ms-2 resize-none outline-none"
                        onChange={handleChange}
                        onBlur={handleBlur}
                      />
                    </div>
                    <ErrorMessage
                      name="title"
                      component={"p"}
                      className="text-[0.7rem] text-red-500"
                    />
                  </article>

                  <article className="w-full mt-[20px] flex justify-center items-center">
                    <button className="w-[80%] rounded-xl bg-blue-400 py-2 cursor-pointer flex justify-center items-center gap-2">
                      {addTaskMutation.isPending && <Loader />}
                      Add
                    </button>
                  </article>
                </Form>
              )}
            </Formik>
          </article>
        </section>
      )}
    </>
  );
};

export default Home;

const TODO_CARD = ({ data }) => {
  const queryClient = useQueryClient();

  const { id, title, completed } = data;

  const deleteTask = async () => {
    const response = await axiosInstance.delete(`/todo/secure/delete/${id}`);
    return response.data;
  };

  const upadetTask = async () => {
    const response = await axiosInstance.put(
      `/todo/secure/update/status/${id}`
    );
    return response.data;
  };

  const updateTaskMutation = useMutation({
    mutationFn: upadetTask,
    mutationKey: "update-task",
    onSuccess: (result) => {
      toast.success("Task Status updated successfully");
      queryClient.invalidateQueries(["all-todo"]);
    },
    onError: (error) => {
      toast.error(getErrorMessage(error));
    },
  });

  const deleteTaskMutation = useMutation({
    mutationFn: deleteTask,
    mutationKey: "delete-task",
    onSuccess: (result) => {
      toast.success("Task deleted successfully");
      queryClient.invalidateQueries(["all-todo"]);
      setDeleteModal(false);
    },
    onError: (error) => {
      toast.error(getErrorMessage(error));
    },
  });

  const [deleteModal, setDeleteModal] = useState(false);

  return (
    <>
      <div className="w-full p-5 h-fit bg-white border border-blue-400 rounded-2xl shadow">
        <h1 className="text-[1.4rem] font-semibold">{title}</h1>
        <h1 className="mt-[20px] text-[0.9rem] text-gray-800">Task id {id}</h1>

        <h1 className="mt-[20px] text-[0.9rem] text-gray-800">
          Status{" "}
          {completed ? (
            <span className="text-[0.8rem] border border-green-400 p-[3px] ms-2 text-green-400 rounded-full">
              Completed
            </span>
          ) : (
            <span className="text-[0.8rem] border border-blue-400 p-[3px] ms-2 text-blue-400 rounded-full">
              Pending
            </span>
          )}
        </h1>

        <div className="w-full flex justify-end items-center gap-1 mt-[20px]">
          {
            !completed && <button
            onClick={() => updateTaskMutation.mutate()}
            className="text-[0.9rem]  p-1 bg-blue-400 rounded-xl cursor-pointer flex justify-center items-center gap-1"
          >
            {updateTaskMutation?.isPending && <Loader />}
            Mark as done
          </button>
          }

          <button
            onClick={() => setDeleteModal(true)}
            className="text-[0.9rem] p-1 bg-blue-400 text-black rounded-xl cursor-pointer flex justify-center items-center gap-1"
          >
            <CiTrash size={16} className="inline" />
            Delete
          </button>
        </div>
      </div>

      {/* Delete Modal */}
      {deleteModal && (
        <section
          className="w-full fixed top-0 left-0 h-screen !bg-black/30 flex justify-center items-center"
          onClick={() => setDeleteModal(false)}
        >
          <div
            className="p-3 bg-white rounded-2xl w-full max-w-[450px]"
            onClick={(e) => e.stopPropagation()}
          >
            <h1>Are you sure want to delete?</h1>

            <div className="w-full mt-[30px] flex justify-end items-center gap-3">
              <button
                className="text-[0.8rem] cursor-pointer flex justify-center items-center"
                onClick={() => deleteTaskMutation.mutate()}
              >
                {deleteTaskMutation.isPending && <Loader />}
                Ok
              </button>
              <button
                className="text-[0.8rem] cursor-pointer"
                onClick={() => setDeleteModal(false)}
              >
                Cancel
              </button>
            </div>
          </div>
        </section>
      )}
    </>
  );
};
