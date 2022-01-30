package pinacolada.cards.pcl.colorless;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.*;
import pinacolada.actions.orbs.RemoveOrb;
import pinacolada.cards.base.*;
import pinacolada.cards.base.cardeffects.GenericEffect;
import pinacolada.cards.base.cardeffects.GenericEffects.GenericEffect_ChannelOrb;
import pinacolada.cards.base.cardeffects.GenericEffects.GenericEffect_GainTempHP;
import pinacolada.cards.pcl.special.OrbCore;
import pinacolada.orbs.pcl.*;
import pinacolada.powers.PCLPower;
import pinacolada.powers.PCLPowerHelper;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

import java.util.HashMap;

public class Merlin extends PCLCard
{

    protected static final HashMap<String, GenericEffect> EFFECTS = new HashMap<>();
    protected static final GenericEffect FALLBACK = GenericEffect.Apply(1, PCLPowerHelper.Swirled);
    public static final PCLCardData DATA = Register(Merlin.class)
            .SetSkill(1, CardRarity.RARE, PCLCardTarget.None)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.Fate)
            .PostInitialize(data -> {
                EFFECTS.put(Air.ORB_ID, GenericEffect.Apply(2, PCLPowerHelper.Swirled));
                EFFECTS.put(Chaos.ORB_ID, new GenericEffect_ChannelOrb(1, null));
                EFFECTS.put(Dark.ORB_ID, GenericEffect.Apply(2, PCLPowerHelper.Rippled));
                EFFECTS.put(Earth.ORB_ID, GenericEffect.Apply(2, PCLPowerHelper.Stoned));
                EFFECTS.put(Fire.ORB_ID, GenericEffect.Apply(2, PCLPowerHelper.Burning));
                EFFECTS.put(Frost.ORB_ID, GenericEffect.Apply(2, PCLPowerHelper.Freezing));
                EFFECTS.put(Lightning.ORB_ID, GenericEffect.Apply(2, PCLPowerHelper.Electrified));
                EFFECTS.put(Metal.ORB_ID, GenericEffect.Gain(1, PCLPowerHelper.Metallicize));
                EFFECTS.put(Plasma.ORB_ID, GenericEffect.Gain(1, PCLPowerHelper.Energized));
                EFFECTS.put(Water.ORB_ID, new GenericEffect_GainTempHP(4));
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
                PCLJUtils.Format(cardData.Strings.EXTENDED_DESCRIPTION[2], EFFECTS.getOrDefault(orb.ID, FALLBACK).GetText(), PCLGameUtilities.GetOrbTooltip(orb)) :
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
                            GenericEffect effect = EFFECTS.getOrDefault(orb.ID, FALLBACK);
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
        private final GenericEffect effect;
        public MerlinPower(AbstractPlayer owner, AbstractCard card, GenericEffect effect)
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
                this.effect.Use(null, player, m != null ? m : PCLGameUtilities.GetRandomEnemy(true));
                this.flash();
            }
        }

        @Override
        public void updateDescription()
        {
            description = FormatDescription(0, PCLJUtils.ModifyString(card.name, w -> "#y" + w), effect.GetText());
        }
    }
}