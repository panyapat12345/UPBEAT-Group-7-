import React from 'react'
import Link from 'next/link'


function SetConfigurationPlan() {
  return (
    <div>
        <li>
        <Link href="/">Home</Link>
      </li>
      <h1>SetConfigurationPlan</h1>
      <li>
       <textarea
          rows={30}
          cols={100}
        ></textarea>
      </li>
      <li>
        <button>
        <Link href="/Part1SetupGame/SetConstructionPlanPlayer1">Next</Link>
        </button>
      </li>
      
    </div>
  )
}

export default SetConfigurationPlan