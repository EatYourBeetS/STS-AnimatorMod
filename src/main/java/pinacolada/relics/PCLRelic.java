package pinacolada.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.relics.*;
import eatyourbeets.utilities.FieldInfo;
import pinacolada.cards.base.PCLCardTooltip;
import pinacolada.dailymods.AllRelicPCLRun;
import pinacolada.dailymods.NoRelics;
import pinacolada.relics.pcl.AbstractMissingPiece;
import pinacolada.resources.CardTooltips;
import pinacolada.resources.GR;
import pinacolada.resources.pcl.PCLDungeonData;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Scanner;

public abstract class PCLRelic extends CustomRelic
{
    protected static final FieldInfo<Float> _offsetX = PCLJUtils.GetField("offsetX", AbstractRelic.class);

    public static AbstractPlayer player;
    public static Random rng;

    public PCLCardTooltip mainTooltip;
    public ArrayList<PCLCardTooltip> tips;

    public static String CreateFullID(Class<? extends PCLRelic> type)
    {
        return GR.PCL.CreateID(type.getSimpleName());
    }

    public PCLRelic(String id, String imageID, RelicTier tier, LandingSound sfx)
    {
        super(id, GR.GetTexture(GR.GetRelicImage(imageID)), tier, sfx);
    }

    public PCLRelic(String id, RelicTier tier, LandingSound sfx)
    {
        this(id, id, tier, sfx);
    }

    public TextureAtlas.AtlasRegion GetPowerIcon()
    {
        final Texture texture = img;
        final int h = texture.getHeight();
        final int w = texture.getWidth();
        final int section = h / 2;
        return new TextureAtlas.AtlasRegion(texture, (w / 2) - (section / 2), (h / 2) - (section / 2), section, section);
    }

    @Override
    public final void updateDescription(AbstractPlayer.PlayerClass c)
    {
        this.description = getUpdatedDescription();
        this.mainTooltip.description = description;
    }

    @Override
    public String getUpdatedDescription()
    {
        return FormatDescription(0, counter);
    }

    @Override
    public AbstractRelic makeCopy()
    {
        try
        {
            return getClass().getConstructor().newInstance();
        }
        catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e)
        {
            PCLJUtils.LogError(this, e.getMessage());
            return null;
        }
    }

    @Override
    public void renderCounter(SpriteBatch sb, boolean inTopPanel)
    {
        if (this.counter >= 0)
        {
            final String text = GetCounterString();
            if (inTopPanel)
            {
                FontHelper.renderFontRightTopAligned(sb, FontHelper.topPanelInfoFont, text,
                        _offsetX.Get(null) + this.currentX + 30.0F * Settings.scale, this.currentY - 7.0F * Settings.scale, Color.WHITE);
            }
            else
            {
                FontHelper.renderFontRightTopAligned(sb, FontHelper.topPanelInfoFont, text,
                        this.currentX + 30.0F * Settings.scale, this.currentY - 7.0F * Settings.scale, Color.WHITE);
            }
        }
    }

    protected String FormatDescription(int index, Object... args)
    {
        return PCLJUtils.Format(DESCRIPTIONS[index], args);
    }

    protected void DisplayAboveCreature(AbstractCreature creature)
    {
        PCLActions.Top.Add(new RelicAboveCreatureAction(creature, this));
    }

    protected String GetCounterString()
    {
        return String.valueOf(counter);
    }

    public boolean IsEnabled()
    {
        return !super.grayscale;
    }

    public boolean SetEnabled(boolean value)
    {
        super.grayscale = !value;
        return value;
    }

    public int SetCounter(int amount)
    {
        setCounter(amount);

        return counter;
    }

    public int AddCounter(int amount)
    {
        setCounter(counter + amount);

        return counter;
    }

    @Override
    public void renderBossTip(SpriteBatch sb)
    {
        PCLCardTooltip.QueueTooltips(tips, Settings.WIDTH * 0.63F, Settings.HEIGHT * 0.63F);
    }

    @Override
    public void renderTip(SpriteBatch sb)
    {
        PCLCardTooltip.QueueTooltips(this);
    }

    @Override
    public void atPreBattle()
    {
        super.atPreBattle();

        ActivateBattleEffect();
    }

    @Override
    public void onVictory()
    {
        super.onVictory();

        DeactivateBattleEffect();
    }

    @Override
    public void onEquip()
    {
        super.onEquip();

        if (PCLGameUtilities.InBattle(true))
        {
            ActivateBattleEffect();
        }
    }

    @Override
    public void onUnequip()
    {
        super.onUnequip();

        if (PCLGameUtilities.InBattle(true))
        {
            DeactivateBattleEffect();
        }
    }

    @Override
    protected void initializeTips()
    {
        if (tips == null)
        {
            tips = new ArrayList<>();
        }
        else
        {
            tips.clear();
        }

        mainTooltip = new PCLCardTooltip(name, description);
        tips.add(mainTooltip);

        final Scanner desc = new Scanner(this.description);
        String s;
        boolean alreadyExists;
        do
        {
            if (!desc.hasNext())
            {
                desc.close();
                return;
            }

            s = desc.next();
            if (s.charAt(0) == '#')
            {
                s = s.substring(2);
            }

            s = s.replace(',', ' ');
            s = s.replace('.', ' ');

            if (s.length() > 4)
            {
                s = s.replace('[', ' ');
                s = s.replace(']', ' ');
            }

            s = s.trim();
            s = s.toLowerCase();

            PCLCardTooltip tip = CardTooltips.FindByName(s);
            if (tip != null && !tips.contains(tip))
            {
                tips.add(tip);
            }
        }
        while (true);
    }

    protected void ActivateBattleEffect()
    {

    }

    protected void DeactivateBattleEffect()
    {

    }

    public static void UpdateRelics(boolean isPCLCharacter)
    {
        if (isPCLCharacter)
        {
            final PCLDungeonData data = GR.PCL.Dungeon;

            if (!ModHelper.isModEnabled(AllRelicPCLRun.ID)) {
                data.RemoveRelic(PenNib.ID);
                data.RemoveRelic(Shuriken.ID);
                data.RemoveRelic(Kunai.ID);
                data.RemoveRelic(StrikeDummy.ID);
                data.RemoveRelic(SneckoEye.ID);
                data.RemoveRelic(SacredBark.ID);
                data.RemoveRelic(RunicPyramid.ID);
                data.RemoveRelic(CeramicFish.ID);
                data.RemoveRelic(TinyHouse.ID);
            }

            data.AddRelic(MarkOfPain.ID, AbstractRelic.RelicTier.BOSS);

            if (ModHelper.isModEnabled(AllRelicPCLRun.ID)) {
                data.AddRelic(BlackBlood.ID, AbstractRelic.RelicTier.BOSS);
                data.AddRelic(RunicCube.ID, AbstractRelic.RelicTier.BOSS);
                data.AddRelic(WristBlade.ID, AbstractRelic.RelicTier.BOSS);
                data.AddRelic(HoveringKite.ID, AbstractRelic.RelicTier.BOSS);
                data.AddRelic(FrozenCore.ID, AbstractRelic.RelicTier.BOSS);
                data.AddRelic(Inserter.ID, AbstractRelic.RelicTier.BOSS);
                data.AddRelic(NuclearBattery.ID, AbstractRelic.RelicTier.BOSS);
                data.AddRelic(HolyWater.ID, AbstractRelic.RelicTier.BOSS);
            }

            if (ModHelper.isModEnabled(NoRelics.ID))
            {
                ArrayList<String> relics = new ArrayList<>();
                relics.addAll(PCLGameUtilities.GetRelicPool(RelicTier.COMMON));
                relics.addAll(PCLGameUtilities.GetRelicPool(RelicTier.UNCOMMON));
                relics.addAll(PCLGameUtilities.GetRelicPool(RelicTier.RARE));
                relics.addAll(PCLGameUtilities.GetRelicPool(RelicTier.SHOP));

                for (String relic : relics)
                {
                    data.RemoveRelic(relic);
                }

                data.AddRelic(Circlet.ID, AbstractRelic.RelicTier.COMMON);
                data.AddRelic(Circlet.ID, AbstractRelic.RelicTier.UNCOMMON);
                data.AddRelic(Circlet.ID, AbstractRelic.RelicTier.RARE);
                data.AddRelic(Circlet.ID, AbstractRelic.RelicTier.SHOP);

                return;
            }

            data.AddRelic(RunicCapacitor.ID, AbstractRelic.RelicTier.SHOP);
            data.AddRelic(TwistedFunnel.ID, AbstractRelic.RelicTier.SHOP);
            data.AddRelic(Brimstone.ID, AbstractRelic.RelicTier.SHOP);
            data.AddRelic(DataDisk.ID, AbstractRelic.RelicTier.SHOP);
            data.AddRelic(SacredBark.ID, AbstractRelic.RelicTier.SHOP);
            data.AddRelic(CloakClasp.ID, AbstractRelic.RelicTier.RARE);
            data.AddRelic(CharonsAshes.ID, AbstractRelic.RelicTier.RARE);
            data.AddRelic(ChampionsBelt.ID, AbstractRelic.RelicTier.RARE);
            data.AddRelic(PaperCrane.ID, AbstractRelic.RelicTier.UNCOMMON);
            data.AddRelic(PaperFrog.ID, AbstractRelic.RelicTier.UNCOMMON);
            data.AddRelic(RedSkull.ID, AbstractRelic.RelicTier.COMMON);

            if (ModHelper.isModEnabled(AllRelicPCLRun.ID)) {
                data.AddRelic(BurningBlood.ID, AbstractRelic.RelicTier.SHOP);
                data.AddRelic(RingOfTheSerpent.ID, AbstractRelic.RelicTier.SHOP);
                data.AddRelic(CrackedCore.ID, AbstractRelic.RelicTier.SHOP);
                data.AddRelic(PureWater.ID, AbstractRelic.RelicTier.SHOP);
                data.AddRelic(SneckoSkull.ID, AbstractRelic.RelicTier.COMMON);
                data.AddRelic(SelfFormingClay.ID, AbstractRelic.RelicTier.UNCOMMON);
                data.AddRelic(NinjaScroll.ID, AbstractRelic.RelicTier.UNCOMMON);
                data.AddRelic(GoldPlatedCables.ID, AbstractRelic.RelicTier.UNCOMMON);
                data.AddRelic(SymbioticVirus.ID, AbstractRelic.RelicTier.UNCOMMON);
                data.AddRelic(Duality.ID, AbstractRelic.RelicTier.UNCOMMON);
                data.AddRelic(MagicFlower.ID, AbstractRelic.RelicTier.RARE);
                data.AddRelic(TheSpecimen.ID, AbstractRelic.RelicTier.RARE);
                data.AddRelic(Tingsha.ID, AbstractRelic.RelicTier.RARE);
                data.AddRelic(ToughBandages.ID, AbstractRelic.RelicTier.RARE);
                data.AddRelic(EmotionChip.ID, AbstractRelic.RelicTier.RARE);
                data.AddRelic(GoldenEye.ID, AbstractRelic.RelicTier.RARE);
                data.AddRelic(Melange.ID, AbstractRelic.RelicTier.SHOP);
            }

            AbstractMissingPiece.RefreshDescription();
            PCLEnchantableRelic.RefreshDescription();
        }
    }

    // Prevents duplicate relics from showing up for the Animator
    // TODO replace your versions of Animator relics with different ones
    public boolean canSpawn() {
        return PCLGameUtilities.IsPlayerClass(GR.PCL.PlayerClass) || (GR.PCL.Config.EnableRelicsForOtherCharacters.Get() && !PCLGameUtilities.IsPlayerClass(eatyourbeets.resources.GR.Animator.PlayerClass));
    }
}
