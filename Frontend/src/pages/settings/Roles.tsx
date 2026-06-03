import { motion } from 'motion/react';
import { ShieldCheck, Users, Plus, Edit, Lock, History, UserCheck, AlertTriangle, Copy, Trash2, Search } from 'lucide-react';
import { useState } from 'react';
import { Modal } from '@/components/ui/Modal';
import { useToast } from '@/components/ui/Toast';

const predefinedRoles = [
  { id: 'R-001', name: 'Super Admin', users: 2, description: 'Full access to all system modules and settings.', isSystem: true },
  { id: 'R-002', name: 'Hotel Manager', users: 5, description: 'Access to hotel operations, bookings, staff, and basic reports.', isSystem: false },
  { id: 'R-003', name: 'Restaurant Manager', users: 4, description: 'Access to POS, menu management, and restaurant reports.', isSystem: false },
  { id: 'R-004', name: 'Receptionist', users: 12, description: 'Access to reservations, room status, and guest profiles.', isSystem: false },
  { id: 'R-005', name: 'Kitchen Staff', users: 18, description: 'Access to kitchen dashboard and ingredient inventory.', isSystem: false },
];

const modules = [
  'Dashboard Analytics',
  'Reservation Module',
  'Room Management',
  'Restaurant POS',
  'Menu Management',
  'Customer Database',
  'Staff Management',
  'Inventory System',
  'Reports & Insights',
  'System Settings'
];

export default function Roles() {
  const { toast } = useToast();
  const [roles, setRoles] = useState(predefinedRoles);
  const [selectedRole, setSelectedRole] = useState(roles[0]);
  const [isAddRoleOpen, setIsAddRoleOpen] = useState(false);
  const [activeTab, setActiveTab] = useState<'matrix' | 'audit'>('matrix');
  const [searchQuery, setSearchQuery] = useState('');

  // Form State
  const [newRoleName, setNewRoleName] = useState('');
  const [newRoleDesc, setNewRoleDesc] = useState('');

  const handleAddRole = (e: React.FormEvent) => {
    e.preventDefault();
    const newRole = {
      id: `R-00${roles.length + 1}`,
      name: newRoleName,
      description: newRoleDesc,
      users: 0,
      isSystem: false
    };
    setRoles([...roles, newRole]);
    setSelectedRole(newRole);
    setIsAddRoleOpen(false);
    setNewRoleName('');
    setNewRoleDesc('');
    toast('Custom role created successfully', 'success');
  };

  const handleSavePermissions = () => {
    toast('Permissions matrix updated', 'success');
  };

  const handleCloneRole = () => {
    const newRole = {
      id: `R-00${roles.length + 1}`,
      name: `${selectedRole.name} (Copy)`,
      description: selectedRole.description,
      users: 0,
      isSystem: false
    };
    setRoles([...roles, newRole]);
    setSelectedRole(newRole);
    toast('Role cloned successfully', 'success');
  };

  const handleDeleteRole = () => {
    if (selectedRole.isSystem) return;
    const filtered = roles.filter(r => r.id !== selectedRole.id);
    setRoles(filtered);
    setSelectedRole(filtered[0]);
    toast('Role deleted', 'success');
  };

  return (
    <div className="h-full flex flex-col gap-8">
      {/* Header */}
      <header className="flex flex-col md:flex-row md:items-center justify-between gap-4 shrink-0">
        <div>
          <h1 className="text-2xl font-serif tracking-tight text-[#F5F2ED]">WOTEGE <span className="text-wotege-gold font-light opacity-60 italic ml-2">Security</span></h1>
          <p className="text-xs text-white/40 mt-1 uppercase tracking-[0.2em]">Roles & Permissions Matrix</p>
        </div>
        <button 
          onClick={() => setIsAddRoleOpen(true)}
          className="px-5 py-2.5 bg-wotege-gold text-black rounded-full text-xs font-bold uppercase tracking-wider hover:shadow-[0_0_15px_rgba(197,160,89,0.4)] transition-all flex items-center shrink-0 w-fit"
        >
            <Plus className="w-4 h-4 mr-1" /> Create Custom Role
        </button>
      </header>
      
      <div className="flex flex-col lg:flex-row gap-8 flex-1 lg:overflow-hidden">
         {/* Roles Sidebar */}
         <div className="w-full lg:w-80 shrink-0 bg-[#111] border border-white/5 rounded-[2rem] p-6 flex flex-col lg:overflow-y-auto no-scrollbar max-h-[400px] lg:max-h-none">
            <h3 className="text-sm font-bold uppercase tracking-widest text-white/80 mb-6 flex items-center">
                <Users className="w-4 h-4 mr-2" /> Defined Roles
            </h3>

            <div className="relative mb-4 shrink-0">
               <Search className="absolute left-3 w-4 h-4 text-white/30 top-1/2 -translate-y-1/2" />
               <input 
                 type="text" 
                 placeholder="Search roles..." 
                 value={searchQuery}
                 onChange={(e) => setSearchQuery(e.target.value)}
                 className="w-full bg-[#141414] border border-white/10 rounded-full pl-9 pr-4 py-2 text-xs text-[#F5F2ED] focus:border-wotege-gold/50 focus:outline-none transition-colors" 
               />
            </div>
            
            <div className="space-y-3">
               {roles.filter(r => r.name.toLowerCase().includes(searchQuery.toLowerCase())).map(role => (
                 <button
                   key={role.id}
                   onClick={() => setSelectedRole(role)}
                   className={`w-full text-left p-4 rounded-2xl transition-all ${
                     selectedRole.id === role.id 
                       ? 'bg-wotege-gold/10 border border-wotege-gold/30 text-wotege-gold shadow-[0_0_20px_rgba(197,160,89,0.1)]' 
                       : 'bg-[#141414] border border-white/5 text-[#F5F2ED] hover:border-white/20'
                   }`}
                 >
                   <div className="flex justify-between items-start mb-1">
                     <span className="font-serif text-lg">{role.name}</span>
                     {role.isSystem && <Lock className="w-3 h-3 text-white/20" />}
                   </div>
                   <div className="flex items-center justify-between text-xs font-sans mt-2">
                     <span className={selectedRole.id === role.id ? 'text-wotege-gold/80' : 'text-white/40'}>{role.users} Users</span>
                     <span className="opacity-50 text-[10px] tracking-widest uppercase">{role.id}</span>
                   </div>
                 </button>
               ))}
            </div>
         </div>
         
         {/* Matrix View */}
         <div className="flex-1 bg-[#111] border border-white/5 rounded-[2rem] p-6 lg:p-8 overflow-y-auto no-scrollbar flex flex-col relative">
            <div className="flex flex-col sm:flex-row sm:items-center justify-between mb-8 pb-8 border-b border-white/5 shrink-0 gap-4">
               <div>
                 <div className="flex items-center gap-3 mb-2">
                   <h2 className="text-2xl font-serif text-[#F5F2ED]">{selectedRole.name}</h2>
                   {selectedRole.isSystem && (
                     <span className="text-[10px] uppercase font-bold tracking-widest border border-white/10 px-2 py-0.5 rounded text-white/40">System Role</span>
                   )}
                 </div>
                 <p className="text-sm text-white/40 max-w-md">{selectedRole.description}</p>
               </div>
               <div className="flex gap-2">
                 <button onClick={handleCloneRole} className="p-3 bg-[#141414] border border-white/10 rounded-xl text-white/60 hover:text-white transition-colors" title="Clone Role">
                   <Copy className="w-5 h-5" />
                 </button>
                 {!selectedRole.isSystem && (
                   <button onClick={handleDeleteRole} className="p-3 bg-[#141414] border border-white/10 rounded-xl text-white/60 hover:text-red-400 hover:border-red-500/30 transition-colors" title="Delete Role">
                     <Trash2 className="w-5 h-5" />
                   </button>
                 )}
                 <button className="p-3 bg-[#141414] border border-white/10 rounded-xl text-white/60 hover:text-white transition-colors" title="Edit Role Details">
                   <Edit className="w-5 h-5" />
                 </button>
               </div>
            </div>
            
            <div className="flex items-center gap-4 mb-6 border-b border-white/5 pb-0 shrink-0">
              <button 
                onClick={() => setActiveTab('matrix')}
                className={`pb-4 text-sm font-bold uppercase tracking-widest transition-colors flex items-center relative ${activeTab === 'matrix' ? 'text-wotege-gold' : 'text-white/40 hover:text-white/80'}`}
              >
                <ShieldCheck className="w-4 h-4 mr-2" /> Access Matrix
                {activeTab === 'matrix' && <motion.div layoutId="roletab" className="absolute bottom-0 left-0 right-0 h-0.5 bg-wotege-gold" />}
              </button>
              <button 
                onClick={() => setActiveTab('audit')}
                className={`pb-4 text-sm font-bold uppercase tracking-widest transition-colors flex items-center relative ${activeTab === 'audit' ? 'text-wotege-gold' : 'text-white/40 hover:text-white/80'}`}
              >
                <History className="w-4 h-4 mr-2" /> Audit Trail
                {activeTab === 'audit' && <motion.div layoutId="roletab" className="absolute bottom-0 left-0 right-0 h-0.5 bg-wotege-gold" />}
              </button>
            </div>
            
            {activeTab === 'matrix' ? (
              <>
                <div className="flex items-center justify-between mb-4 shrink-0">
                  <span className="text-xs text-white/40">Toggle permissions to update role access. Changes apply immediately upon saving.</span>
                  <button 
                    onClick={handleSavePermissions}
                    disabled={selectedRole.isSystem}
                    className="text-xs font-bold uppercase tracking-widest text-wotege-gold hover:text-white transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
                  >
                      Save Changes
                  </button>
                </div>
                <div className="overflow-x-auto flex-1 border border-white/5 rounded-xl">
                  <table className="w-full text-left border-collapse min-w-[600px]">
                    <thead>
                      <tr className="border-b border-white/5 bg-[#141414]">
                        <th className="px-6 py-4 text-[10px] uppercase font-bold tracking-widest text-white/40 w-1/3">Module / Resource</th>
                        <th className="px-6 py-4 text-[10px] uppercase font-bold tracking-widest text-white/40 text-center">View</th>
                        <th className="px-6 py-4 text-[10px] uppercase font-bold tracking-widest text-white/40 text-center">Create</th>
                        <th className="px-6 py-4 text-[10px] uppercase font-bold tracking-widest text-white/40 text-center">Edit</th>
                        <th className="px-6 py-4 text-[10px] uppercase font-bold tracking-widest text-white/40 text-center">Delete</th>
                      </tr>
                    </thead>
                    <tbody>
                      {modules.map((mod, i) => {
                        const isSuperAdmin = selectedRole.name === 'Super Admin';
                        const isSystem = selectedRole.isSystem;
                        // Mock logical permissions
                        const hasAccess = isSuperAdmin || (selectedRole.name === 'Receptionist' && mod.includes('Reservation')) || (selectedRole.name === 'Restaurant Manager' && mod.includes('Restaurant')) || Math.random() > 0.5;
                        const canWrite = isSuperAdmin || (hasAccess && Math.random() > 0.4);
                        const canDelete = isSuperAdmin || (canWrite && Math.random() > 0.8);

                        return (
                          <tr key={i} className="border-b last:border-0 border-white/5 hover:bg-white/5 transition-colors">
                            <td className="px-6 py-4 font-medium text-sm text-[#F5F2ED]">{mod}</td>
                            <td className="px-6 py-4 text-center">
                                <input type="checkbox" defaultChecked={hasAccess} className={`accent-wotege-gold w-4 h-4 rounded cursor-pointer ${isSystem ? 'opacity-50 cursor-not-allowed' : ''}`} disabled={isSystem} />
                            </td>
                            <td className="px-6 py-4 text-center">
                                <input type="checkbox" defaultChecked={canWrite} className={`accent-wotege-gold w-4 h-4 rounded cursor-pointer ${isSystem ? 'opacity-50 cursor-not-allowed' : ''}`} disabled={isSystem} />
                            </td>
                            <td className="px-6 py-4 text-center">
                                <input type="checkbox" defaultChecked={canWrite} className={`accent-wotege-gold w-4 h-4 rounded cursor-pointer ${isSystem ? 'opacity-50 cursor-not-allowed' : ''}`} disabled={isSystem} />
                            </td>
                            <td className="px-6 py-4 text-center">
                                <input type="checkbox" defaultChecked={canDelete} className={`accent-red-500 w-4 h-4 rounded cursor-pointer ${isSystem ? 'opacity-50 cursor-not-allowed' : ''}`} disabled={isSystem} />
                            </td>
                          </tr>
                        );
                      })}
                    </tbody>
                  </table>
                </div>
              </>
            ) : (
              <div className="flex-1 space-y-4">
                {[
                  { user: 'Saman Perera', action: 'Modified Permissions', date: '12 May 2026, 14:30', detail: 'Added "Delete" rights to Inventory System' },
                  { user: 'Admin System', action: 'Assigned User', date: '10 May 2026, 09:12', detail: 'User "Jane Doe" assigned to role' },
                  { user: 'System Init', action: 'Role Created', date: '01 Jan 2026, 00:00', detail: 'Role initialized by deployment' },
                ].map((log, idx) => (
                  <div key={idx} className="bg-[#141414] border border-white/5 p-4 rounded-xl flex gap-4">
                    <div className="w-8 h-8 rounded-full bg-white/5 flex items-center justify-center shrink-0">
                      <UserCheck className="w-4 h-4 text-white/40" />
                    </div>
                    <div>
                      <div className="text-sm font-medium text-[#F5F2ED] mb-1">{log.action}</div>
                      <div className="text-xs text-white/60 mb-2">{log.detail}</div>
                      <div className="flex items-center gap-2 text-[10px] uppercase tracking-widest text-white/40">
                        <span>{log.user}</span>
                        <span>•</span>
                        <span>{log.date}</span>
                      </div>
                    </div>
                  </div>
                ))}
              </div>
            )}
         </div>
      </div>

      <Modal isOpen={isAddRoleOpen} onClose={() => setIsAddRoleOpen(false)} title="Create Custom Role">
        <form onSubmit={handleAddRole} className="space-y-6">
          <div className="bg-wotege-gold/10 border border-wotege-gold/20 p-4 rounded-xl flex gap-3 text-wotege-gold text-sm mb-4">
            <AlertTriangle className="w-5 h-5 shrink-0" />
            <p>Custom roles inherit no permissions by default. You must manually assign access rights after creation.</p>
          </div>
          
          <div>
            <label className="block text-[10px] uppercase font-bold tracking-widest text-white/40 mb-2">Role Name <span className="text-red-500">*</span></label>
            <input 
              type="text" 
              value={newRoleName}
              onChange={(e) => setNewRoleName(e.target.value)}
              placeholder="e.g. Senior Bartender" 
              className="w-full bg-[#141414] border border-white/10 rounded-xl px-4 py-3 text-sm text-[#F5F2ED] focus:border-wotege-gold/50 focus:outline-none transition-colors" 
              required 
            />
          </div>
          <div>
            <label className="block text-[10px] uppercase font-bold tracking-widest text-white/40 mb-2">Description</label>
            <textarea 
              value={newRoleDesc}
              onChange={(e) => setNewRoleDesc(e.target.value)}
              placeholder="Briefly describe the responsibilities of this role..." 
              rows={3} 
              className="w-full bg-[#141414] border border-white/10 rounded-xl px-4 py-3 text-sm text-[#F5F2ED] focus:border-wotege-gold/50 focus:outline-none transition-colors resize-none"
            />
          </div>
          <div>
             <label className="block text-[10px] uppercase font-bold tracking-widest text-white/40 mb-2">Department</label>
             <select className="w-full bg-[#141414] border border-white/10 rounded-xl px-4 py-3 text-sm text-[#F5F2ED] focus:border-wotege-gold/50 focus:outline-none transition-colors">
               <option>General Administration</option>
               <option>Front Desk / Hotel</option>
               <option>Food & Beverage</option>
               <option>Housekeeping</option>
               <option>Finance</option>
             </select>
          </div>
          
          <div className="flex justify-end pt-4">
            <button 
              type="submit" 
              className="py-3 px-8 bg-wotege-gold text-black rounded-xl font-bold text-xs uppercase tracking-wider hover:shadow-[0_0_20px_rgba(197,160,89,0.3)] transition-all active:scale-[0.98]"
            >
              Initialize Role
            </button>
          </div>
        </form>
      </Modal>
    </div>
  );
}
