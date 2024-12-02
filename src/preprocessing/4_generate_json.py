import json
import csv

def load_train_data(train_file):
    """Load the JSON data from train.json."""
    with open(train_file, 'r', encoding='utf-8') as f:
        return json.load(f)

def filter_repos(train_data, repo_ids):
    """Filter the train data to include only those repos whose repo_id is in the list of repo_ids."""
    return [repo for repo in train_data if repo['repo_id'] in repo_ids]

def save_filtered_data(filtered_data, output_file):
    """Save the filtered JSON data to a new file."""
    with open(output_file, 'w', encoding='utf-8') as f:
        json.dump(filtered_data, f, indent=4)

def load_repo_ids(csv_file):
    """Load repo_ids from the CSV file."""
    repo_ids = []
    with open(csv_file, 'r', encoding='utf-8') as f:
        reader = csv.DictReader(f)
        for row in reader:
            repo_ids.append(int(row['repo_id']))
    return repo_ids

if __name__ == '__main__':
    # Paths to the necessary files
    train_file = r"D:/DataPreprocesing/Data/split_train/split_train_10.json"
    csv_file = r"D:/DataPreprocesing/folder_analysis_results_10.csv"
    output_file = r"D:/DataPreprocesing/Data/split_train_10_v2.json"
    
    # Load the data
    train_data = load_train_data(train_file)
    repo_ids = load_repo_ids(csv_file)
    
    # Filter the train data based on repo_ids
    filtered_data = filter_repos(train_data, repo_ids)
    
    # Save the filtered data to a new JSON file
    save_filtered_data(filtered_data, output_file)
    
    print(f"Filtered data has been saved to {output_file}")
