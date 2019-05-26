package eatyourbeets.cards.animator;

import com.evacipated.cardcrawl.mod.stslib.actions.defect.TriggerPassiveAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.orbs.Earth;

public class DwarfShaman extends AnimatorCard
{
    public static final String ID = CreateFullID(DwarfShaman.class.getSimpleName());

    public DwarfShaman()
    {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);

        Initialize(5, 0, 0);

        AddExtendedDescription();

        SetSynergy(Synergies.GoblinSlayer);
    }

    private void OnChannel(Object state, AbstractGameAction action)
    {
        if (state == this && action != null)
        {
            GameActionsHelper.AddToTop(new TriggerPassiveAction(1));
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.DamageTarget(p, m, this.damage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.BLUNT_LIGHT);

        if (HasActiveSynergy())
        {
            GameActionsHelper.Callback(new ChannelAction(new Earth(), true), this::OnChannel, this);
        }
        else
        {
            GameActionsHelper.ChannelOrb(new Earth(), true);
        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeDamage(3);
        }
    }
}