package eatyourbeets.cards.animator.beta.DateALive;

import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.StartupCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.cardManipulation.MakeTempCard;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.interfaces.markers.Spellcaster;
import eatyourbeets.interfaces.subscribers.OnAddedToDrawPileSubscriber;
import eatyourbeets.utilities.GameActions;

public class MukuroHoshimiya extends AnimatorCard implements StartupCard, Spellcaster, OnAddedToDrawPileSubscriber
{
    public static final EYBCardData DATA = Register(MukuroHoshimiya.class).SetAttack(2, CardRarity.RARE, EYBAttackType.Elemental);

    public MukuroHoshimiya()
    {
        super(DATA);

        Initialize(4, 0);
        SetUpgrade(2, 0);

        SetSynergy(Synergies.DateALive);
    }

    @Override
    protected void OnUpgrade() {
        SetScaling(1, 1, 0);
    }

    @Override
    protected float GetInitialDamage()
    {
        return player.drawPile.size();
    }

    @Override
    public boolean atBattleStartPreDraw()
    {
        OnAddedToDrawPile(false, MakeTempCard.Destination.Random);

        return false;
    }

    @Override
    public void OnAddedToDrawPile(boolean visualOnly, MakeTempCard.Destination destination)
    {
        GameActions.Top.Callback(__ ->
        {
            CardGroup group = player.drawPile;
            if (group.group.remove(this))
            {
                group.group.add(group.size() - Math.min(group.size(), 6), this);
            }
        });
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SMASH);
    }
}