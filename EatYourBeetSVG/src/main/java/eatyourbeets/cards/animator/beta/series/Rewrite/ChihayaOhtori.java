package eatyourbeets.cards.animator.beta.series.Rewrite;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.special.RefreshHandLayout;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.modifiers.CostModifiers;
import eatyourbeets.misc.GenericEffects.GenericEffect_Apply;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.powers.common.ForcePower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;

public class ChihayaOhtori extends AnimatorCard
{
    public static final EYBCardData DATA = Register(ChihayaOhtori.class).SetAttack(3, CardRarity.UNCOMMON, EYBAttackType.Normal);

    private static final CardEffectChoice choices = new CardEffectChoice();

    public ChihayaOhtori()
    {
        super(DATA);

        Initialize(12, 0, 7, 1);
        SetUpgrade(0, 0, -2);
        SetMartialArtist();

        SetSynergy(Synergies.Rewrite);
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
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SMASH);

        String[] text = DATA.Strings.EXTENDED_DESCRIPTION;

        if (choices.TryInitialize(this))
        {
            choices.AddEffect(text[0], this::Effect1);
            choices.AddEffect(new GenericEffect_Apply(TargetHelper.Player(), PowerHelper.Artifact, magicNumber));
        }

        choices.Select(1, m);
    }

    private void Effect1(AbstractCard card, AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Top.FetchFromPile(name, 1, player.discardPile)
        .SetOptions(false, false)
        .SetFilter(c -> c.hasTag(MARTIAL_ARTIST))
        .AddCallback(cards ->
        {
            if (cards.size() > 0)
            {
                GameActions.Bottom.Motivate(cards.get(0), 1);
                GameActions.Bottom.Add(new RefreshHandLayout());
            }
        });
    }

    public void RefreshCost()
    {
        int force = GameUtilities.GetPowerAmount(AbstractDungeon.player, ForcePower.POWER_ID);
        CostModifiers.For(this).Set(-1*(force / magicNumber));
    }
}