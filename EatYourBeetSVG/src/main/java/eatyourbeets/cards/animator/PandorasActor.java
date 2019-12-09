package eatyourbeets.cards.animator;

import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.StartupCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.cards.Synergies;
import eatyourbeets.interfaces.OnCallbackSubscriber;
import eatyourbeets.utilities.GameActionsHelper_Legacy; import eatyourbeets.utilities.GameActions;

public class PandorasActor extends AnimatorCard implements StartupCard
{
    public static final String ID = Register(PandorasActor.class.getSimpleName(), EYBCardBadge.Special);

    public PandorasActor()
    {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);

        Initialize(0, 5);

        SetSynergy(Synergies.Overlord, true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(this.block);
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeBlock(3);
        }
    }

    @Override
    public boolean atBattleStartPreDraw()
    {
        GameActions.Bottom.Callback(__ ->
        {
            AbstractCard copy = this.makeStatEquivalentCopy();
            copy.applyPowers();
            copy.use(AbstractDungeon.player, null);
            copy.purgeOnUse = true;
            copy.freeToPlayOnce = true;
            AnimatorCard.SetLastCardPlayed(copy);
        });

        return true;
    }
}