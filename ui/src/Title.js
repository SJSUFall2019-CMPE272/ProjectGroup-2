import React from "react"
import Icon from './Icon.png'; 

import './Title.css';



console.log(Icon); 

class Title extends React.Component{
    render(){
        return (
            <div className="Title" width="80%" >
            
                <br/>
            <center>
            <div width="90%" ><img src={Icon} alt="Rinnovation" width="10%" height="100%"/></div>
            </center>
            
            <div><center>Optimize your home renovation</center></div>
            <br/>
            
            
            </div>
            );
    
    }
            
}
      
    
    


export default Title;