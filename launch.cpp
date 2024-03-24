#include<cstdlib>
#include<iomanip>
#include<iostream>
#include<sstream>
#include<ranges>
int main(int argl,char**argv)
{
    using namespace std;
    ostringstream oss;
    oss << "java -cp lib/commons-text-1.11.0.jar:lib/jortho.jar:out.jar App";
    for(auto i : views::iota(1, argl))
        oss << ' ' << quoted(argv[i]);
    system(oss.str().c_str());
    return 0;
}
