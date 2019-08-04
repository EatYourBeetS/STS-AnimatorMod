package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard_Status;
import eatyourbeets.cards.Synergies;

public class GoblinSoldier extends AnimatorCard_Status
{
    public static final String ID = CreateFullID(GoblinSoldier.class.getSimpleName());

    public GoblinSoldier()
    {
        super(ID, 1, CardRarity.COMMON, CardTarget.NONE);

        Initialize(0, 0, 2);

        SetSynergy(Synergies.GoblinSlayer);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();
        GameActionsHelper.DrawCard(AbstractDungeon.player, 1);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (this.dontTriggerOnUseCard)
        {
            GameActionsHelper.DamageTarget(p, p, this.magicNumber, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL);
        }
    }
}