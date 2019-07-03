package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.unique.ArmamentsAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.orbs.Earth;
import eatyourbeets.utilities.GameActionsHelper;

public class DwarfShaman extends AnimatorCard
{
    public static final String ID = CreateFullID(DwarfShaman.class.getSimpleName());

    public DwarfShaman()
    {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);

        Initialize(4, 0, 0);

        AddExtendedDescription();

        SetSynergy(Synergies.GoblinSlayer);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.DamageTarget(p, m, this.damage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        GameActionsHelper.ChannelOrb(new Earth(), true);

        if (upgraded || HasActiveSynergy())
        {
            GameActionsHelper.AddToBottom(new ArmamentsAction(false));
        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeDamage(2);
        }
    }
}