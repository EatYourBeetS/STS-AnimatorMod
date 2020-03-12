package eatyourbeets.cards.animator.beta.DateALive;

import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.StartupCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.unique.CodexAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.cardManipulation.MakeTempCard;
import eatyourbeets.cards.animator.series.Konosuba.Wiz;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.interfaces.markers.Spellcaster;
import eatyourbeets.interfaces.subscribers.OnAddedToDrawPileSubscriber;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActions;

import java.util.ArrayList;

public class MukuroHoshimiya extends AnimatorCard implements StartupCard, Spellcaster, OnAddedToDrawPileSubscriber {
    public static final EYBCardData DATA = Register(MukuroHoshimiya.class).SetAttack(2, CardRarity.RARE, EYBAttackType.Elemental);

    public boolean IsBeingAddedToDrawPile;

    public MukuroHoshimiya() {
        super(DATA);

        Initialize(0, 0);
        SetUpgrade(0, 0);

        if (upgraded)
        {
            SetScaling(1, 1, 0);
        }

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
        OnAddedToDrawPile(false, MakeTempCard.Destination.Random);

        return false;
    }

    @Override
    public void OnAddedToDrawPile(boolean visualOnly, MakeTempCard.Destination destination) {
        GameActions.Top.Callback(__ ->
        {
            CardGroup group = player.drawPile;
            group.group.remove(this);
            group.group.add(group.size() - Math.min(group.size(), 6), this);
        });
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SMASH);
    }

    private void PutOnSixthCardInDrawPile()
    {
        if (IsBeingAddedToDrawPile)
        {
            return;
        }

        IsBeingAddedToDrawPile = true;
        GameActions.Bottom.MakeCardInDrawPile(makeCopy())
        .AddCallback(c ->
        {
            CardGroup group = player.drawPile;
            if (group.contains(c))
            {
                group.removeCard(c);
                group.group.add(group.size() - Math.min(group.size(), 6), c);
                IsBeingAddedToDrawPile = false;
            }
        });
    }
}