package eatyourbeets.ui.animator.combat;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import eatyourbeets.effects.SFX;
import eatyourbeets.effects.VFX;
import eatyourbeets.effects.vfx.megacritCopy.HemokinesisEffect2;
import eatyourbeets.interfaces.subscribers.*;
import eatyourbeets.monsters.PlayerMinions.UnnamedDoll;
import eatyourbeets.monsters.UnnamedReign.UnnamedDoll.TheUnnamed_Doll;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.resources.GR;
import eatyourbeets.ui.GUIElement;
import eatyourbeets.utilities.Colors;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

import java.util.ArrayList;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.player;

public class UnnamedDollManager extends GUIElement implements OnStartOfTurnSubscriber, OnStartOfTurnPostDrawSubscriber, OnEndOfTurnFirstSubscriber, OnModifyDamageLastSubscriber
{
    public static final int DEFAULT_MAX_SLOTS = 4;
    public final UnnamedDollSlot[] Slots = new UnnamedDollSlot[DEFAULT_MAX_SLOTS];
    public int MaxSlots = DEFAULT_MAX_SLOTS;

    public UnnamedDollSlot GetSlot(int slot)
    {
        return Slots[slot];
    }

    public ArrayList<UnnamedDoll> GetAll()
    {
        final ArrayList<UnnamedDoll> result = new ArrayList<>();
        for (int i = 0; i < MaxSlots; i++)
        {
            final UnnamedDollSlot slot = Slots[i];
            if (slot.CanAct())
            {
                result.add(slot.Doll);
            }
        }

        return result;
    }

    public ArrayList<AbstractMonster> GetAllAsMonsters()
    {
        final ArrayList<AbstractMonster> result = new ArrayList<>();
        for (int i = 0; i < MaxSlots; i++)
        {
            final UnnamedDollSlot slot = Slots[i];
            if (slot.CanAct())
            {
                result.add(slot.Doll);
            }
        }

        return result;
    }

    public boolean Any()
    {
        for (int i = 0; i < MaxSlots; i++)
        {
            if (Slots[i].CanAct())
            {
                return true;
            }
        }

        return false;
    }

    public UnnamedDollSlot GetAvailableSlot()
    {
        for (int i = 0; i < MaxSlots; i++)
        {
            final UnnamedDollSlot slot = Slots[i];
            if (slot.IsAvailable())
            {
                return slot;
            }
        }

        return null;
    }

    public void OnDeath(UnnamedDoll doll)
    {
        GameActions.Bottom.VFX(new HemokinesisEffect2(doll.hb.cX, doll.hb.cY, player.hb.cX, player.hb.cY).SetScale(0.7f), 0.35f);
        GameActions.Bottom.Callback(doll, (skip, __) ->
        {
            GameActions.Bottom.StackPower(null, new StrengthPower(player, 1));

            for (UnnamedDoll d : GetAll())
            {
                if (d != skip)
                {
                    GameActions.Bottom.StackPower(null, new StrengthPower(d, 1));
                }
            }
        });

        for (OnPlayerMinionActionSubscriber subscriber : CombatStats.onPlayerMinionAction.GetSubscribers())
        {
            subscriber.OnMinionDeath(doll);
        }
    }

    public void Activate(UnnamedDoll doll, boolean endOfTurn)
    {
        doll.takeTurn();

        for (OnPlayerMinionActionSubscriber subscriber : CombatStats.onPlayerMinionAction.GetSubscribers())
        {
            subscriber.OnMinionActivation(doll, endOfTurn);
        }
    }

    public void ChangeIntent(UnnamedDoll doll, AbstractMonster.Intent targetIntent, boolean forceChange)
    {
        if (forceChange || (doll.intent != AbstractMonster.Intent.STUN && CombatStats.TryActivateSemiLimited(TheUnnamed_Doll.ID + "Intent")))
        {
            AbstractMonster.Intent previous = doll.intent;
            doll.SwitchIntent(targetIntent);
            AbstractMonster.Intent current = doll.intent;

            if (previous != current)
            {
                for (OnPlayerMinionActionSubscriber subscriber : CombatStats.onPlayerMinionAction.GetSubscribers())
                {
                    subscriber.OnMinionIntentChanged(doll, previous, current);
                }

                if (!forceChange)
                {
                    SFX.Play(SFX.GUARDIAN_ROLL_UP, 2.3f, 2.5f, 0.9f);
                }
                
                GameUtilities.RefreshHandLayout(false);
            }
        }
        else
        {
            SFX.Play(SFX.CARD_REJECT);
        }
    }

    public void LoseSlot()
    {
        if (MaxSlots > 0)
        {
            final UnnamedDollSlot slot = Slots[MaxSlots -= 1];
            if (slot.CanAct())
            {
                Sacrifice(slot.Doll);
            }
        }
    }

    public void OverSummon(int amount)
    {
        GameActions.Bottom.ModifyDollsMaxHP(amount);
    }

    public void Sacrifice(UnnamedDoll doll)
    {
        SFX.Play(SFX.ATTACK_SCYTHE, 0.9f, 0.95f);
        GameEffects.List.Add(VFX.Clash(doll.hb).SetColor(Colors.Violet(1)));
        GameUtilities.RefreshHandLayout(false);
        doll.die(true);
    }

    public boolean CanSummon(boolean triggerEvents)
    {
        for (OnPlayerMinionActionSubscriber subscriber : CombatStats.onPlayerMinionAction.GetSubscribers())
        {
            if (!subscriber.CanSummonMinion(triggerEvents))
            {
                return false;
            }
        }

        return MaxSlots > 0;
    }

    public UnnamedDoll Summon(UnnamedDoll.SummonData data)
    {
        final UnnamedDoll doll = new UnnamedDoll(data);
        Slots[data.Slot].Doll = doll;

        for (OnPlayerMinionActionSubscriber subscriber : CombatStats.onPlayerMinionAction.GetSubscribers())
        {
            subscriber.OnMinionSummon(doll);
        }

        GameUtilities.RefreshHandLayout(false);
        return doll;
    }

    @Override
    public void OnStartOfTurn()
    {
        for (UnnamedDoll doll : GetAll())
        {
            doll.applyStartOfTurnPowers();
        }
    }

    @Override
    public void OnStartOfTurnPostDraw()
    {
        for (UnnamedDoll doll : GetAll())
        {
            doll.applyStartOfTurnPostDrawPowers();
        }
    }

    @Override
    public void OnEndOfTurnFirst(boolean isPlayer)
    {
        for (UnnamedDoll doll : GetAll())
        {
            Activate(doll, true);
            doll.applyEndOfTurnTriggers();
        }
    }

    @Override
    public int OnModifyDamageLast(AbstractCreature target, DamageInfo info, int damage)
    {
        if (target == player && info.type == DamageInfo.DamageType.NORMAL)
        {
            for (UnnamedDoll doll : GetAll())
            {
                if (damage > 0 && GameUtilities.IsDefending(doll.intent) && doll.currentHealth > 0)
                {
                    final int amount = Math.min(damage, doll.currentHealth);
                    doll.damage(new DamageInfo(info.owner, amount, DamageInfo.DamageType.NORMAL));
                    damage -= amount;
                }
            }
        }

        return damage;
    }

    // ====================== //
    //  GUI Rendering/Update  //
    // ====================== //

    public void Initialize()
    {
        CombatStats.onStartOfTurn.Subscribe(this);
        CombatStats.onStartOfTurnPostDraw.Subscribe(this);
        CombatStats.onEndOfTurnFirst.Subscribe(this);
        CombatStats.onModifyDamageLast.Subscribe(this);
        MaxSlots = DEFAULT_MAX_SLOTS;

        for (int i = 0; i < Slots.length; i++)
        {
            if (Slots[i] == null)
            {
                Slots[i] = new UnnamedDollSlot(i);
            }
        }
    }

    public void Clear()
    {
        for (UnnamedDollSlot slot : Slots)
        {
            slot.Clear();
        }
    }

    public void Update()
    {
        if (player == null || player.hand == null || AbstractDungeon.overlayMenu.energyPanel.isHidden)
        {
            return;
        }

        for (int i = 0; i < MaxSlots; i++)
        {
            Slots[i].Update();
        }

        GR.UI.AddPreRender(this::Render);
    }

    public void Render(SpriteBatch sb)
    {
        for (int i = 0; i < MaxSlots; i++)
        {
            Slots[i].Render(sb);
        }
    }
}