import React, { PureComponent } from 'react'
class UploadFile extends PureComponent {
    constructor() {
        super()
        this.state = {
            files: [],
            result: ""
        }
        this.uploadFile = this.uploadFile.bind(this);
    }
    uploadFile() {
        const files = this.state.files;
        if (files.length > 0) {
            const file = new FormData();
            file.append('file', files[0]);
            const requestOption = {
                method: 'POST',
                body: file
            }
            fetch("http://localhost:8080/upload", requestOption).then(res => {
                console.log("res: ", res, " res stat:",res['status']);
                if(res['status']==200){
                    this.setState({result: "FILE UPLOAD SUCCESSFUL" });
                }
                return res;
            });
        }
    }
    render() {
        return (
            <center><div>
                    <p> File Result: {this.state.result} </p>
                        <input type="file" onChange={e => this.setState({
                            files: e.target.files
                        })}>
                        </input>
                        &nbsp; &nbsp;

                        <button onClick={this.uploadFile}>Upload</button>

            </div></center>
        )
    }
}

export default UploadFile