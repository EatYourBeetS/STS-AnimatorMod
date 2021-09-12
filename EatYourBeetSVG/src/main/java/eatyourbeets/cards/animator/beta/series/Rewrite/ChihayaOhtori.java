package eatyourbeets.cards.animator.beta.series.Rewrite;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.special.RefreshHandLayout;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.modifiers.CostModifiers;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;

public class ChihayaOhtori extends AnimatorCard
{
    public static final EYBCardData DATA = Register(ChihayaOhtori.class).SetAttack(3, CardRarity.UNCOMMON, EYBAttackType.Normal).SetSeriesFromClassPackage();

    private static final CardEffectChoice choices = new CardEffectChoice();

    public ChihayaOhtori()
    {
        super(DATA);

        Initialize(12, 0, 3, 3);
        SetUpgrade(0, 0, -2);
        SetAffinity_Green(2, 0, 0);
        SetAffinity_Orange(1, 0, 0);
    }

    @Override
    public void atTurnStart()
    {
        super.atTurnStart();

        Refresh(null);
    }

    @Override
    public void triggerOnOtherCardPlayed(AbstractCard c)
    {
        super.triggerOnOtherCardPlayed(c);

        GameActions.Bottom.Callback(this::RefreshCost);
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        RefreshCost();
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SMASH);

        String[] text = DATA.Strings.EXTENDED_DESCRIPTION;

        if (choices.TryInitialize(this))
        {
            choices.AddEffect(text[0], this::Effect1);
            choices.AddEffect(text[1], this::Effect2);
        }

        choices.Select(1, m);
    }

    private void Effect1(AbstractCard card, AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Top.FetchFromPile(name, 1, player.discardPile)
        .SetOptions(false, false)
        .SetFilter(c -> (c instanceof AnimatorCard && ((AnimatorCard) c).affinities.GetLevel(Affinity.Orange) > 0))
        .AddCallback(cards ->
        {
            if (cards.size() > 0)
            {
                GameActions.Bottom.Motivate(cards.get(0), 1);
                GameActions.Bottom.Add(new RefreshHandLayout());
            }
        });
    }

    private void Effect2(AbstractCard card, AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Top.GainTemporaryArtifact(secondaryValue);
    }

    public void RefreshCost()
    {
        int orange = GetHandAffinity(Affinity.Orange, false);
        CostModifiers.For(this).Set(-1*(orange / magicNumber));
    }
}