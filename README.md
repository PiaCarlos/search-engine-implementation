# Search Engine Project – COMP 250 Final Project

## Overview
This project implements a **simplified search engine in Java**, combining **graphs, hash tables, and sorting algorithms**. It simulates crawling a web database, building a word index, computing PageRanks, and returning search results ranked by relevance. The project demonstrates **algorithmic efficiency, data structure integration, and modular design**.

## Key Features
- **Crawling & Indexing:**  
  - BFS-based traversal using a queue to explore pages and links.  
  - Builds a **directed graph** (`MyWebGraph`) with vertices as URLs and edges as hyperlinks.  
  - Updates a **hash-based word index** mapping words → URLs, case-insensitive and without duplicates.  

- **Search Queries:**  
  - Supports single-word queries.  
  - Returns **ranked URLs** using `fastSort` (merge sort) or `slowSort` (bubble sort) from `Sorting.java`.  

- **Sorting Utilities (`Sorting.java`):**  
  - `fastSort`: merge sort implementation, O(n log n), sorts URLs based on PageRank.  
  - `slowSort`: simple bubble sort, O(n²), for testing and comparison.  
  - Fully generic using `<K, V extends Comparable<V>>`.  

- **Testing & Performance:**  
  - `SortingTest.java` uses JUnit to verify correctness and performance.  
  - Compares `fastSort` vs `slowSort` speed and validates descending order.  
