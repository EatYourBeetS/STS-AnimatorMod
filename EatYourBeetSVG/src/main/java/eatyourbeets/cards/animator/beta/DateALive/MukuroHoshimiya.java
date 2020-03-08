package eatyourbeets.cards.animator.beta.DateALive;

import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.StartupCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.interfaces.markers.Spellcaster;
import eatyourbeets.utilities.GameActions;

public class MukuroHoshimiya extends AnimatorCard implements StartupCard, Spellcaster {
    public static final EYBCardData DATA = Register(MukuroHoshimiya.class).SetAttack(2, CardRarity.RARE, EYBAttackType.Elemental);

    public MukuroHoshimiya() {
        super(DATA);

        Initialize(0, 0);
        SetUpgrade(0, 0);

        SetSynergy(Synergies.DateALive);
    }

    @Override
    protected float GetInitialDamage()
    {
        return player.drawPile.size();
    }

    @Override
    public boolean atBattleStartPreDraw()
    {
        PutOnSixthCardInDrawPile();

        return true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.NONE);
    }

    private void PutOnSixthCardInDrawPile()
    {
        if (player.drawPile.size() <= 5)
        {
            AbstractDungeon.player.discardPile.moveToBottomOfDeck(this);
        }
        else
        {
            AbstractDungeon.player.discardPile.group.add(5, this);
        }
    }
}