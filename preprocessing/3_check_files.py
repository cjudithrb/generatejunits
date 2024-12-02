import os
import shutil

def check_and_delete_folders(input_dir, output_file):
    """
    Check for empty folders or folders containing only 'log.txt' with size 0 or specific content.
    Logs the repo_id and results to a file and deletes the corresponding folders if necessary.
    """
    results = {
        'Empty Folders': 0,
        'Folders with log.txt (Size 0)': 0,
        'Folders with log.txt (No Matches Found)': 0
    }
    
    with open(output_file, 'w') as f:
        f.write("repo_id,Category\n")  # Write header
        
        for root, dirs, files in os.walk(input_dir):
            for dir_name in dirs:
                dir_path = os.path.join(root, dir_name)
                repo_id = dir_name  # Assuming dir_name is the repo_id
                try:
                    # List all files and directories in the folder
                    items = os.listdir(dir_path)
                    
                    if len(items) == 0:
                        # Empty folder, delete it
                        shutil.rmtree(dir_path)
                        results['Empty Folders'] += 1
                        f.write(f"repo_id: {repo_id},Empty Folder\n")
                    elif len(items) == 1 and items[0] == 'log.txt':
                        log_path = os.path.join(dir_path, 'log.txt')
                        size = os.path.getsize(log_path)
                        if size == 0:
                            # Folder contains only log.txt with size 0, delete it
                            shutil.rmtree(dir_path)
                            results['Folders with log.txt (Size 0)'] += 1
                            f.write(f"repo_id: {repo_id},Folder with log.txt (Size 0)\n")
                        else:
                            # Check if log.txt contains specific text
                            with open(log_path, 'r', encoding='utf-8') as log_file:
                                content = log_file.read()
                                if "No se encontraron coincidencias." in content:
                                    results['Folders with log.txt (No Matches Found)'] += 1
                                    f.write(f"repo_id: {repo_id},Folder with log.txt (No Matches Found)\n")
                except Exception as e:
                    print(f"Error checking or deleting folder {dir_path}: {e}")

    # Print results summary
    print("\nSummary of Results:")
    for category, count in results.items():
        print(f"{category}: {count}")

if __name__ == '__main__':
    # Define the directory to check and the output file
    input_dir = r"D:/DataPreprocesing/tmp_output_10/"
    output_file = r"D:/DataPreprocesing/folder_analysis_results_10.csv"
    
    check_and_delete_folders(input_dir, output_file)
