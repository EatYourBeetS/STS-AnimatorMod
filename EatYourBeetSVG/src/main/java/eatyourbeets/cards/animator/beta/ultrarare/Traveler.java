package eatyourbeets.cards.animator.beta.ultrarare;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.actions.defect.EvokeSpecificOrbAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DarkOrbEvokeAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.VoidCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Dark;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.OfferingEffect;
import com.megacrit.cardcrawl.vfx.combat.RoomTintEffect;
import eatyourbeets.cards.base.*;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnPostDrawSubscriber;
import eatyourbeets.orbs.animator.Aether;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.powers.animator.ElementalMasteryPower;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.*;

import java.util.ArrayList;
import java.util.HashSet;

public class Traveler extends AnimatorCard_UltraRare implements OnStartOfTurnPostDrawSubscriber
{
    public static final EYBCardData DATA = Register(Traveler.class).SetSkill(3, CardRarity.SPECIAL, EYBCardTarget.None).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.GenshinImpact);
    private final RotatingList<EYBCardPreview> previews = new RotatingList<>();

    public Traveler.Form currentForm;

    public enum Form
    {
        None,
        Aether,
        Lumine
    }

    public Traveler()
    {
        this(Traveler.Form.None);
    }

    private Traveler(Traveler.Form form)
    {
        super(DATA);

        Initialize(0, 0, 20, 2);
        SetUpgrade(0, 0, 5, 0);
        SetAffinity_Light(2);
        SetAffinity_Dark(2);
        SetUnique(true, true);
        SetEthereal(true);
        ChangeForm(form);
    }

    @Override
    public void initializeDescription()
    {
        super.initializeDescription();

        if (cardText != null)
        {
            tooltips.add(GR.Tooltips.ElementalExposure);
            if (this.currentForm != Form.Aether)
                tooltips.add(GR.Tooltips.ElementalMastery);
        }
    }

    @Override
    protected void OnUpgrade()
    {
        previews.Clear();
        if (timesUpgraded % 3 == 0)
        {
            upgradeSecondaryValue(1);
        }

        upgradedSecondaryValue = true;
    }

    @Override
    public EYBCardPreview GetCardPreview()
    {
        if (previews.Count() == 0) {
            Traveler formA = ((Traveler) makeStatEquivalentCopy()).ChangeForm(Form.Aether);
            Traveler formB = ((Traveler) makeStatEquivalentCopy()).ChangeForm(Form.Lumine);
            previews.Add((new EYBCardPreview(formA, false)));
            previews.Add((new EYBCardPreview(formB, false)));
        }
        ArrayList<EYBCardPreview> cards = previews.GetInnerList();
        switch (this.currentForm)
        {
            case Aether:
            {
                return cards.size() >= 2 ? cards.get(1) : super.GetCardPreview();
            }

            case Lumine:
            {
                return cards.size() >= 1 ? cards.get(0) : super.GetCardPreview();
            }
            default:
            {
                EYBCardPreview currentPreview;
                if (Gdx.input.isKeyJustPressed(Input.Keys.CONTROL_LEFT) || Gdx.input.isKeyJustPressed(Input.Keys.CONTROL_RIGHT))
                {
                    currentPreview = previews.Next(true);
                }
                else
                {
                    currentPreview = previews.Current();
                }

                currentPreview.isMultiPreview = true;
                return currentPreview;
            }
        }
    }

    public Traveler ChangeForm(Traveler.Form form)
    {
        this.currentForm = form;

        switch (form)
        {
            case None:
            {
                LoadImage(null);
                cardText.OverrideDescription(null, true);
                tooltips.add(GR.Tooltips.ElementalMastery);
                tooltips.add(GR.Tooltips.ElementalExposure);
                SetExhaust(false);
                break;
            }

            case Aether:
            {
                LoadImage("_Aether");
                this.cardText.OverrideDescription(cardData.Strings.EXTENDED_DESCRIPTION[0], true);
                tooltips.add(GR.Tooltips.ElementalExposure);
                SetExhaust(false);
                break;
            }

            case Lumine:
            {
                LoadImage("_Lumine");
                this.cardText.OverrideDescription(cardData.Strings.EXTENDED_DESCRIPTION[1], true);
                SetExhaust(true);
                break;
            }
        }

        return this;
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        this.ChangeForm(currentForm == Traveler.Form.Aether ? Traveler.Form.Lumine : Traveler.Form.Aether);
        GameActions.Bottom.MoveCard(this, player.drawPile)
                .ShowEffect(true, true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        switch (currentForm)
        {
            case Aether:
                int totalMastery = magicNumber;

                HashSet<String> uniqueOrbs = new HashSet<>();
                RandomizedList<AbstractOrb> randomOrbs = new RandomizedList<>();

                for (AbstractOrb orb : player.orbs) {
                    if (GameUtilities.IsValidOrb(orb)) {
                        randomOrbs.Add(orb);
                    }
                }

                for (int i = 0; i < secondaryValue; i++) {
                    AbstractOrb orb = randomOrbs.Retrieve(rng, false);
                    AbstractOrb orbCopy = orb.makeCopy();
                    if (!uniqueOrbs.contains(orb.ID)) {
                        uniqueOrbs.add(orb.ID);
                        totalMastery += 5;
                    }
                    GameActions.Bottom.Add(new EvokeSpecificOrbAction(orbCopy));
                }

                GameActions.Bottom.ChannelOrbs(Aether::new, 1);
                GameActions.Bottom.StackPower(new ElementalMasteryPower(p, totalMastery));
                break;
            case Lumine:
                GameActions.Top.Add(new VFXAction(new OfferingEffect(), Settings.FAST_MODE ? 0.1F : 0.5F));
                for (int i = 0; i < secondaryValue; i++) {
                    GameActions.Bottom.StackPower(TargetHelper.Enemies(), PowerHelper.Frail, 1);
                    GameActions.Bottom.StackPower(TargetHelper.Enemies(), PowerHelper.Vulnerable, 1);
                    GameActions.Bottom.StackPower(TargetHelper.Enemies(), PowerHelper.Weak, 1);
                }
                Traveler other = (Traveler) makeStatEquivalentCopy();
                CombatStats.onStartOfTurnPostDraw.Subscribe(other);
                GameActions.Bottom.MakeCardInDiscardPile(new VoidCard());
                GameActions.Bottom.MakeCardInDiscardPile(new VoidCard());
        }

    }

    @Override
    public AbstractCard makeStatEquivalentCopy()
    {
        Traveler other = (Traveler) super.makeStatEquivalentCopy();

        other.ChangeForm(this.currentForm);

        return other;
    }

    @Override
    public void OnStartOfTurnPostDraw()
    {
        int orbCount = CombatStats.OrbsEvokedThisCombat().size();
        if (orbCount > 0)
        {
            GameEffects.Queue.ShowCardBriefly(this);

            AbstractDungeon.effectsQueue.add(new RoomTintEffect(Color.BLACK.cpy(), 0.8F, 2.0F+ (orbCount/10.0F), true));
            AbstractDungeon.effectsQueue.add(new BorderLongFlashEffect(new Color(1.0F, 1.0F, 1.0F, 0.5F)));

            int startIdx = Math.max(orbCount - magicNumber, 0);

            for (int i = startIdx; i < orbCount; i++)
            {
                AbstractOrb orb = CombatStats.OrbsEvokedThisCombat().get(i);
                if (Dark.ORB_ID.equals(orb.ID)) {
                    GameActions.Bottom.Add(new DarkOrbEvokeAction(new DamageInfo(AbstractDungeon.player, orb.passiveAmount, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.FIRE));
                }
                else {
                    orb.onStartOfTurn();
                    orb.onEndOfTurn();
                }
            }

            CombatStats.onStartOfTurnPostDraw.Unsubscribe(this);
        }
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);

        if (currentForm == Traveler.Form.None)
        {
            CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            Traveler formA = ((Traveler) makeStatEquivalentCopy()).ChangeForm(Form.Aether);
            Traveler formB = ((Traveler) makeStatEquivalentCopy()).ChangeForm(Form.Lumine);
            group.group.add(formA);
            group.group.add(formB);

            GameActions.Bottom.SelectFromPile(name, 1, group)
                    .SetOptions(false, false)
                    .AddCallback(cards ->
                    {
                        if (cards != null && cards.size() > 0)
                        {
                            Traveler card = (Traveler) cards.get(0);

                            ChangeForm(card.currentForm);
                            GameUtilities.ModifyMagicNumber(card, this.magicNumber, false);
                            GameUtilities.ModifySecondaryValue(card, this.secondaryValue, false);
                        }
                    });
        }
    }
}