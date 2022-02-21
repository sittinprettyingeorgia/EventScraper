import React, { useEffect, useState } from 'react';
import { LIST } from './constants/pageConstants';

function App() {
  const [data, setData] = useState();
  const [query, setQuery] = useState();

  useEffect(() => {
    const postData = async() => {
      let pages = {
        "typeOfData": "event",
        "pages": LIST
      };

      let jsonObj = JSON.stringify(pages);

      fetch('/api/crawl',{
        method:'post',
        mode:'cors',
        headers: {
          'content-type':'application/json'
        },
        body: jsonObj
      })
          .then(response => response.json())
          .then(json => console.log(json))
          .catch(e => console.log(e));
    };

    const getData = async() => {
      fetch('api/getData')
          .then(response => response.json())
          .then(json => setData(json))
          .catch(e => console.log(e));
    };

    postData();
    getData();

  },[]);

  const handleChange = (e) => {
    setQuery(e.target.value);
  };

  const handleClick = () => {

  };

  return (
    <div className="App">
      <p>You can request event data by start date, end date, location, name, or website.</p>
      <p>Enter a search by location by entering "Las Vegas".</p>
      <p>Enter a search by start date by entering "2022-03-30".</p>
      <p>You will be shown all events that match you query.</p>
      <p>Alternatively you can see all data sorted either ascending or descending order.</p>
      <p> type "asc" for ascending and "desc" for descening.</p>
      <input type="text" className="input" onChange={e => handleChange}/>Filter/Sort Data
      <button onClick={}>Search</button>
    </div>
  );
}

export default App;
