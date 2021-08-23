package eatyourbeets.cards.animator.beta.special;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.curses.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.beta.curse.Curse_JunTormented;
import eatyourbeets.cards.animator.curse.Curse_GriefSeed;
import eatyourbeets.cards.animator.curse.Curse_Nutcracker;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.AnimatorStrings;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.TargetHelper;

public class TakashiNatsume_Circle extends AnimatorCard
{
    public enum Form
    {
        None,
        Curse_Delusion,
        Curse_Depression,
        Curse_GriefSeed,
        Curse_Greed,
        Curse_JunTormented,
        Curse_Nutcracker,
        Decay,
        Doubt,
        Necronomicurse,
        Normality,
        Pain,
        Parasite,
        Regret,
        Shame
    }

    private static final AnimatorStrings.Actions ACTIONS = GR.Animator.Strings.Actions;
    private static final int DAMAGE_DECAY = 2;
    public static final EYBCardData DATA = Register(TakashiNatsume_Circle.class).SetSkill(1, CardRarity.RARE, EYBCardTarget.None).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.NatsumeYuujinchou);
    private TakashiNatsume_Circle.Form currentForm;

    public TakashiNatsume_Circle()
    {
        super(DATA);

        Initialize(0, 0, 1, 2);
        SetUpgrade(0, 0, 0, 1);

        SetAffinity_Light(1);
        SetAffinity_Blue(2);
        SetExhaust(true);
        SetHealing(true);
    }

    protected void ChangeForm(TakashiNatsume_Circle.Form form) {
        currentForm = form;
        //TODO
        switch (form) {
            case Curse_JunTormented:
                cardText.OverrideDescription(DATA.Strings.DESCRIPTION + " NL  NL " + ACTIONS.ApplyToALL(2, GR.Tooltips.Weak, true), true);
            case Decay:
                damage = DAMAGE_DECAY * magicNumber;
            case Doubt:
                cardText.OverrideDescription(DATA.Strings.DESCRIPTION + " NL  NL " + ACTIONS.ApplyToALL(2, GR.Tooltips.Weak, true), true);
            case Normality:
                cardText.OverrideDescription(DATA.Strings.DESCRIPTION + " NL  NL " + DATA.Strings.EXTENDED_DESCRIPTION[2], true);
            case Regret:
                cardText.OverrideDescription(DATA.Strings.DESCRIPTION + " NL  NL " + DATA.Strings.EXTENDED_DESCRIPTION[1], true);
            case Shame:
                cardText.OverrideDescription(DATA.Strings.DESCRIPTION + " NL  NL " + ACTIONS.ApplyToALL(2, GR.Tooltips.Frail, true), true);
            default:
                cardText.OverrideDescription(DATA.Strings.DESCRIPTION, true);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.SelectFromPile(name, magicNumber, player.hand, player.discardPile)
                .SetOptions(false, true)
                .SetFilter(c -> c.type == CardType.CURSE)
                .AddCallback(cards -> {
                    if (cards.size() > 0) {
                        for (AbstractCard c : cards) {
                            for (int i = 0; i < secondaryValue; i++) {
                                PlayCurseEffect(c);
                            }
                            GameActions.Bottom.Exhaust(c);
                        }
                    }
                });
    }

    private void PlayCurseEffect(AbstractCard c) {
        //if (c instanceof Curse_Delusion) {
        //    GameActions.Bottom.Callback(() ->
        //    {
        //        AbstractCard card = null;
        //        RandomizedList<AbstractCard> possible = new RandomizedList<>();
        //        for (AbstractCard ca : player.drawPile.group)
        //        {
        //            if (ca.costForTurn >= 0 && ca.hasTag(AUTOPLAY))
        //            {
        //                possible.Add(ca);
        //            }
        //        }

        //        if (possible.Size() > 0)
        //        {
        //            card = possible.Retrieve(rng);
        //        }

        //        if (card instanceof EYBCard)
        //        {
        //            ((EYBCard) card).SetAutoplay(false);
        //        }
        //    });
        //}
        //else if (c instanceof Curse_Depression) {
        //    GameActions.Bottom.Draw(2);
        //}
        if (c instanceof Curse_GriefSeed) {
            int[] damageMatrix = DamageInfo.createDamageMatrix(1, true);
            GameActions.Bottom.DealDamageToAll(damageMatrix, DamageInfo.DamageType.THORNS, AttackEffects.FIRE);
        }
        //else if (c instanceof Curse_Greed) {
        //    GameActions.Bottom.Motivate();
        //}
        else if (c instanceof Curse_JunTormented) {
            GameActions.Bottom.ApplyWeak(TargetHelper.Enemies(), 1);
            GameActions.Bottom.ApplyFrail(TargetHelper.Enemies(), 1);
        }
        else if (c instanceof Curse_Nutcracker) {
            GameActions.Bottom.Heal(4);
        }
        else if (c instanceof Decay) {
            int[] damageMatrix = DamageInfo.createDamageMatrix(2, true);
            GameActions.Bottom.DealDamageToAll(damageMatrix, DamageInfo.DamageType.THORNS, AttackEffects.FIRE);
        }
        else if (c instanceof Doubt) {
            GameActions.Bottom.ApplyWeak(TargetHelper.Enemies(), 1);
        }
        //else if (c instanceof Normality) {
        //    int i = 0;
        //    for (AbstractMonster mo : GameUtilities.GetEnemies(true)) {
        //        if (i >= 3) {
        //            GameActions.Bottom.ApplyPower(player, new StunMonsterPower(mo, 1));
        //        }
        //        i++;
        //    }
        //}
        else if (c instanceof Pain) {
            GameActions.Bottom.StackPower(player, new TakashiNatsumePower(player, 1));
        }
        else if (c instanceof Regret) {
            int[] damageMatrix = DamageInfo.createDamageMatrix(player.hand.size(), true);
            GameActions.Bottom.DealDamageToAll(damageMatrix, DamageInfo.DamageType.THORNS, AttackEffects.FIRE);
        }
        else if (c instanceof Shame) {
            GameActions.Bottom.ApplyFrail(TargetHelper.Enemies(), 1);
        }
    }


    public static class TakashiNatsumePower extends AnimatorPower
    {
        public TakashiNatsumePower(AbstractPlayer owner, int amount)
        {
            super(owner, TakashiNatsume_Circle.DATA);

            this.amount = amount;
            updateDescription();
        }

        public void atEndOfRound() {
            GameActions.Bottom.RemovePower(owner, owner, this);
        }

        public void onAfterCardPlayed(AbstractCard card) {
            int[] damageMatrix = DamageInfo.createDamageMatrix(this.amount, true);
            GameActions.Bottom.DealDamageToAll(damageMatrix, DamageInfo.DamageType.THORNS, AttackEffects.SLASH_DIAGONAL);
        }

        @Override
        public void updateDescription()
        {
            description = FormatDescription(0, amount);
        }
    }
}