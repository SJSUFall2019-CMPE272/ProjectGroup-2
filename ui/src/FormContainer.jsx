import React, { Component } from "react";
import axios from 'axios';
/* Import Components */
import CheckBox from "./Components/CheckBox";
import Input from "./Components/Input";
import TextArea from "./Components/TextArea";
import Select from "./Components/Select";
import Button1 from "./Components/Button";
import styles from './Components/Button.css';


class FormContainer extends React.Component {
    constructor(props) {
        super(props);
    
        this.state = {
          newUser: {
            zipcode: "",
            room: "",
            
          },
    
          roomOptions: ["Bedroom", "Bathroom", "Livingroom", "Kitchen"],
          
        };
        this.handleFullName = this.handleFullName.bind(this);
        this.handleInput = this.handleInput.bind(this);
    }

    handleFullName(e) {
        let value = e.target.value;
        this.setState(
          prevState => ({
            newUser: {
              ...prevState.newUser,
              zipcode: value
            }
          }),
          () => console.log("======================checking state -========================",this.state.newUser)
        );
      }

      handleInput(e) {
        let value = e.target.value;
        let name = e.target.name;
        this.setState(
          prevState => ({
            newUser: {
              ...prevState.newUser,
              [name]: value
            }
          }),
          () => console.log(this.state.newUser)
        );
      }

      handleClearForm(e) {
        e.preventDefault();
        this.setState({
          newUser: {
            zipcode: "",
            room: "",
          }
        });
      }

      handleFormSubmit(e) {
        e.preventDefault();
        let userData = this.state.newUser;
        console.log("=====================suerdata=====================",userData);
        //axios
        fetch("http://localhost:3001/api", {
          method: "POST",
          body: JSON.stringify(userData),
          headers: {
            Accept: "application/json",
            "Content-Type": "application/json"
          }
        }).then(response => {
          response.json().then(data => {
            console.log("Successful   " + data);
          });
        }
        
        );

        // axios.post(`localhost:3001/api`)
        // .then(res => {
        //   const mydata = res.data;
        //   console.log("====================mydata==========================",mydata);
        // })
      }

    render() {
    return (
      <div>
        <form className="container-fluid" onSubmit={this.handleFormSubmit.bind(this)}>
        <Input
          inputType={"text"}
          //title={"Zip Code"}
          name={"zipcode"}
          value={this.state.newUser.zipcode}
          placeholder={"Enter zipcode"}
          handleChange={this.handleFullName}
          
        />{" "}
        {/* Name of the user */}
        
        <Select
          //title={"Room"}
          name={"room"}
          options={this.state.roomOptions}
          value={this.state.newUser.room}
          placeholder={"Select room to upgrade"}
          handleChange={this.handleInput}
          class={Select}
        />{" "}

        <Button1
          action={this.handleFormSubmit.bind(this)}
          type={"primary"}
          title={"Submit"}
          styles={"buttonStyle"}
          
          
        />{"  "}
        
        <Button1
          action={this.handleClearForm}
          type={"secondary"}
          title={"Clear"}
          
        />{" "}
       

      </form>
      </div>
      
    );
  }
}




const buttonStyle = {
  margin: "10px 10px 10px 10px",
  
};



export default FormContainer;
