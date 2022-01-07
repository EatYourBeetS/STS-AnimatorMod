package pinacolada.cards.pcl.colorless;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.*;
import eatyourbeets.interfaces.delegates.ActionT3;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.actions.orbs.RemoveOrb;
import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.pcl.special.OrbCore;
import pinacolada.orbs.pcl.*;
import pinacolada.powers.PCLPower;
import pinacolada.powers.special.SwirledPower;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

import java.util.HashMap;

public class Merlin extends PCLCard
{
    protected enum MerlinEffect {
        Air(GR.PCL.Strings.Actions.Apply(2, GR.Tooltips.Swirled, true), (c, p, m) -> PCLActions.Delayed.StackPower(player, new SwirledPower(m, 2))),
        Chaos(GR.PCL.Strings.Actions.ChannelRandomOrbs(1, true), (c, p, m) -> PCLActions.Delayed.ChannelRandomOrbs(1)),
        Dark(GR.PCL.Strings.Actions.Apply(2, GR.Tooltips.Constricted, true), (c, p, m) -> PCLActions.Delayed.ApplyConstricted(TargetHelper.Normal(m), 2)),
        Earth(GR.PCL.Strings.Actions.Apply(2, GR.Tooltips.Weak, true), (c, p, m) -> PCLActions.Delayed.ApplyWeak(TargetHelper.Normal(m), 2)),
        Fire(GR.PCL.Strings.Actions.Apply(2, GR.Tooltips.Burning, true), (c, p, m) -> PCLActions.Delayed.ApplyBurning(TargetHelper.Normal(m), 2)),
        Frost(GR.PCL.Strings.Actions.Apply(2, GR.Tooltips.Freezing, true), (c, p, m) -> PCLActions.Delayed.ApplyFreezing(TargetHelper.Normal(m), 2)),
        Lightning(GR.PCL.Strings.Actions.Apply(2, GR.Tooltips.Electrified, true), (c, p, m) -> PCLActions.Delayed.ApplyElectrified(TargetHelper.Normal(m), 2)),
        Plasma(GR.PCL.Strings.Actions.GainAmount(1, GR.Tooltips.Energized, true), (c, p, m) -> PCLActions.Delayed.GainEnergyNextTurn(1)),
        Water(GR.PCL.Strings.Actions.GainAmount(3, GR.Tooltips.TempHP, true), (c, p, m) -> PCLActions.Delayed.GainTemporaryHP(3));

        private final String text;
        private final ActionT3<PCLCard, AbstractPlayer, AbstractMonster> action;

        MerlinEffect(String text, ActionT3<PCLCard, AbstractPlayer, AbstractMonster> action) {
            this.text = text;
            this.action = action;
        }
    }

    protected static final HashMap<String, MerlinEffect> EFFECTS = new HashMap<>();
    public static final PCLCardData DATA = Register(Merlin.class)
            .SetSkill(1, CardRarity.RARE, eatyourbeets.cards.base.EYBCardTarget.None)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.Fate)
            .PostInitialize(data -> {
                EFFECTS.put(Air.ORB_ID, MerlinEffect.Air);
                EFFECTS.put(Chaos.ORB_ID, MerlinEffect.Chaos);
                EFFECTS.put(Dark.ORB_ID, MerlinEffect.Dark);
                EFFECTS.put(Earth.ORB_ID, MerlinEffect.Earth);
                EFFECTS.put(Fire.ORB_ID, MerlinEffect.Fire);
                EFFECTS.put(Frost.ORB_ID, MerlinEffect.Frost);
                EFFECTS.put(Lightning.ORB_ID, MerlinEffect.Lightning);
                EFFECTS.put(Plasma.ORB_ID, MerlinEffect.Plasma);
                EFFECTS.put(Water.ORB_ID, MerlinEffect.Water);
            });

    public Merlin()
    {
        super(DATA);

        Initialize(0, 1, 1, 3);
        SetExhaust(true);

        SetAffinity_Blue(1, 0, 2);
    }

    @Override
    protected String GetRawDescription(Object... args)
    {
        AbstractOrb orb = PCLGameUtilities.InGame() && PCLGameUtilities.InBattle() ? PCLGameUtilities.GetFirstOrb(null) : null;
        return super.GetRawDescription(PCLGameUtilities.IsValidOrb(orb) ?
                PCLJUtils.Format(cardData.Strings.EXTENDED_DESCRIPTION[2], EFFECTS.getOrDefault(orb.ID, MerlinEffect.Earth).text, PCLGameUtilities.GetOrbTooltip(orb)) :
                cardData.Strings.EXTENDED_DESCRIPTION[1]);
    }

    @Override
    public void OnDrag(AbstractMonster m)
    {
        super.OnDrag(m);

        initializeDescription();
    }

    @Override
    public boolean cardPlayable(AbstractMonster m)
    {
        return player.filledOrbCount() > 0;
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);

        final AbstractOrb orb = PCLGameUtilities.GetFirstOrb(null);
        if (PCLGameUtilities.IsValidOrb(orb))
        {
            if (upgraded) {
                PCLActions.Bottom.EvokeOrb(1, orb);
            }
            else {
                PCLActions.Bottom.Add(new RemoveOrb(orb));
            }

            PCLActions.Delayed.SelectFromHand(name, magicNumber, false)
                    .SetMessage(GR.PCL.Strings.HandSelection.GenericBuff)
                    .AddCallback(cards ->
                    {
                        if (cards.size() > 0)
                        {
                            AbstractCard card = cards.get(0);
                            MerlinEffect effect = EFFECTS.getOrDefault(orb.ID, MerlinEffect.Earth);
                            PCLActions.Bottom.ApplyPower(new MerlinPower(p, card, effect)).AllowDuplicates(true);
                        }
                    });

            if (PCLGameUtilities.GetOrbCount(orb.ID) >= secondaryValue) {
                PCLActions.Bottom.Add(OrbCore.SelectCoreAction(name, 1)
                        .AddCallback(cards ->
                        {
                            for (AbstractCard c : cards)
                            {
                                PCLActions.Bottom.MakeCardInHand(c).SetUpgrade(upgraded, false);
                            }
                        }));
            }
        }
    }

    public static class MerlinPower extends PCLPower
    {
        private final AbstractCard card;
        private final MerlinEffect effect;
        public MerlinPower(AbstractPlayer owner, AbstractCard card, MerlinEffect effect)
        {
            super(owner, Merlin.DATA);

            this.card = card;
            this.effect = effect;

            updateDescription();
        }

        @Override
        public void onPlayCard(AbstractCard card, AbstractMonster m)
        {
            super.onPlayCard(card, m);

            if (this.card == card)
            {
                this.effect.action.Invoke(null, player, m != null ? m : PCLGameUtilities.GetRandomEnemy(true));
                this.flash();
            }
        }

        @Override
        public void updateDescription()
        {
            description = FormatDescription(0, card.name.replace(" ", " #y"), effect.text);
        }
    }
}