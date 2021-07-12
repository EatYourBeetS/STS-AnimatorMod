package eatyourbeets.cards.animator.beta.ultrarare;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.RoomTintEffect;
import com.megacrit.cardcrawl.vfx.combat.WhirlwindEffect;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnPostDrawSubscriber;
import eatyourbeets.orbs.animator.Aether;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.animator.ElementalMasteryPower;
import eatyourbeets.powers.animator.TravelerAbyssPower;
import eatyourbeets.utilities.*;

public class Traveler extends AnimatorCard implements OnStartOfTurnPostDrawSubscriber
{
    public static final EYBCardData DATA = Register(Traveler.class).SetSkill(2, CardRarity.SPECIAL, EYBCardTarget.None).SetColor(CardColor.COLORLESS);

    public Traveler.Form currentForm;

    public enum Form
    {
        None,
        Aether,
        Lumine
    }

    public Traveler()
    {
        this(Traveler.Form.None, false);
    }

    private Traveler(Traveler.Form form, boolean upgraded)
    {
        super(DATA);

        Initialize(0, 0, 10, 2);
        SetUpgrade(0, 0, 2, 0);
        SetUnique(true, true);
        SetEthereal(true);
        SetSynergy(Synergies.GenshinImpact);
        this.upgraded = upgraded;
        ChangeForm(form);
    }

    public void ChangeForm(Traveler.Form form)
    {
        this.currentForm = form;

        switch (form)
        {
            case None:
            {
                LoadImage(null);
                cardText.OverrideDescription(null, true);
                break;
            }

            case Aether:
            {
                LoadImage("_Aether");
                this.cardText.OverrideDescription(cardData.Strings.EXTENDED_DESCRIPTION[0], true);
                break;
            }

            case Lumine:
            {
                LoadImage("_Lumine");
                this.cardText.OverrideDescription(cardData.Strings.EXTENDED_DESCRIPTION[1], true);
                break;
            }
        }
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        GameActions.Bottom.RemovePower(player, player, TravelerAbyssPower.POWER_ID);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        switch (currentForm)
        {
            case Aether:
                int idxStart = p.filledOrbCount() - p.maxOrbs;
                for (int i = Math.max(idxStart,0); i < secondaryValue + idxStart; i++)
                {
                    AbstractOrb orb = p.orbs.get(i);
                    if (!(orb instanceof EmptyOrbSlot) && !Aether.ORB_ID.equals(orb.ID)) {
                        GameActions.Bottom.StackPower(new ElementalMasteryPower(p, magicNumber));
                    }
                }
                GameActions.Bottom.VFX(new WhirlwindEffect(), 0f);
                GameActions.Bottom.ChannelOrbs(Aether::new, secondaryValue);
                break;
            case Lumine:
                for (AbstractCreature target : GameUtilities.GetEnemies(true)) {
                    GameActions.Bottom.StackPower(TargetHelper.Normal(target), JUtils.GetRandomElement(GameUtilities.GetCommonDebuffs()), secondaryValue);
                }
                if (CombatStats.OrbsEvokedThisCombat().size() > 0)
                {
                    Traveler other = (Traveler) makeStatEquivalentCopy();
                    other.ChangeForm(this.currentForm);
                    CombatStats.onStartOfTurnPostDraw.Subscribe(other);
                }
        }

    }

    @Override
    public void OnStartOfTurnPostDraw()
    {
        int orbCount = CombatStats.OrbsEvokedThisCombat().size();
        if (orbCount > 0)
        {
            GameEffects.Queue.ShowCardBriefly(this);

            AbstractDungeon.effectsQueue.add(new RoomTintEffect(Color.BLACK.cpy(), 0.8F));
            AbstractDungeon.effectsQueue.add(new BorderLongFlashEffect(new Color(1.0F, 1.0F, 1.0F, 0.5F)));

            int startIdx = Math.max(orbCount - magicNumber, 0);

            for (int i = startIdx; i < orbCount; i++)
            {
                AbstractOrb orb = CombatStats.OrbsEvokedThisCombat().get(i);
                orb.onStartOfTurn();
                orb.onEndOfTurn();
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
            group.group.add(new Traveler(Form.Aether, upgraded));
            group.group.add(new Traveler(Form.Lumine, upgraded));

            GameActions.Bottom.SelectFromPile(name, 1, group)
                    .SetOptions(false, false)
                    .AddCallback(cards ->
                    {
                        if (cards != null && cards.size() > 0)
                        {
                            Traveler card = (Traveler) cards.get(0);

                            ChangeForm(card.currentForm);
                        }
                    });
        }

        GameActions.Bottom.StackPower(new TravelerAbyssPower(player));
    }
}